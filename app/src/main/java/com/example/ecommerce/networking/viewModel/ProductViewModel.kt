package com.example.ecommerce.networking.viewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.koin.ProductRepository
import com.example.ecommerce.networking.model.Product
import com.example.ecommerce.networking.model.Products
import com.example.ecommerce.utils.network.ConnectivityObserver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProductViewModel(
    private val productRepository: ProductRepository,
    connectivityObserver: ConnectivityObserver
) : ViewModel() {

    private val _productState = MutableStateFlow<ProductState>(ProductState.Error("Unknown Error"))
    private val productState: StateFlow<ProductState> = _productState

    var isLoading = mutableStateOf(false)
    var isSuccess = mutableStateOf(false)
    var isError = mutableStateOf(false)
    var errorMessage = mutableStateOf("")

    var products = mutableStateListOf<Product>()

    private val limit = 10
    var skip = -20

    val networkStatus: StateFlow<ConnectivityObserver.Status> =
        connectivityObserver.observe()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = ConnectivityObserver.Status.Unavailable
            )


    sealed class ProductState {
        data class Success(val products: Products) : ProductState()
        data class Error(val message: String) : ProductState()
    }


    fun getProducts() {
        viewModelScope.launch {
            try {
                isLoading.value = true
                if (networkStatus.value == ConnectivityObserver.Status.Unavailable) {
                    _productState.value = ProductState.Error("No Internet Connection")
                    isLoading.value = false
                    return@launch
                }
                skip += limit
                val response = productRepository.getProducts(limit, skip)
                if (response.isSuccessful && response.body() != null) {
                    _productState.value = ProductState.Success(response.body()!!)
                } else {
                    _productState.value = ProductState.Error(response.message())
                }
            } catch (e: Exception) {
                _productState.value = ProductState.Error(e.message ?: "Unknown Error")
            } finally {
                observeProducts()
            }
        }
    }

    private fun observeProducts() {
        viewModelScope.launch {
            productState.collect { state ->
                when (state) {
                    is ProductState.Error -> {
                        isLoading.value = false
                        isError.value = true
                        errorMessage.value = state.message
                    }
                    is ProductState.Success -> {
                        val newProducts = ArrayList<Product>()
                        newProducts.addAll(products)
                        newProducts.addAll(state.products.products)
                        products = newProducts.distinctBy { it.id }.toMutableStateList()

                        isLoading.value = false
                        isSuccess.value = true
                    }
                }
            }
        }
    }
}