package com.example.ecommerce.networking.viewModel

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.koin.ProductRepository
import com.example.ecommerce.networking.model.Product
import com.example.ecommerce.networking.model.Products
import com.example.ecommerce.room.AppDatabase
import com.example.ecommerce.room.ProductEntity
import com.example.ecommerce.utils.network.ConnectivityObserver
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProductViewModel(
    private val productRepository: ProductRepository,
    connectivityObserver: ConnectivityObserver,
    application: Application
) : ViewModel() {

    private val _productState = MutableStateFlow<ProductState>(ProductState.Error("Unknown Error"))
    private val productState: StateFlow<ProductState> = _productState

    private val productDao = AppDatabase.getDatabase(application).productDao()

    var isLoading = mutableStateOf(false)
    var isSuccess = mutableStateOf(false)
    var isError = mutableStateOf(false)
    var errorMessage = mutableStateOf("")

    var products = mutableStateListOf<Product>()

    private val limit = 10
    private var skip = 0

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

    init {
        viewModelScope.launch {
            getLocalProducts().collect { updatedProducts ->
                insertProducts(updatedProducts)
            }
        }
    }

    fun getProductsList() {
        viewModelScope.launch {
            try {
                isLoading.value = true
                if (networkStatus.value == ConnectivityObserver.Status.Unavailable) {
                    _productState.value = ProductState.Error("No Internet Connection")
                    isLoading.value = false
                    return@launch
                }
                skip = products.size
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
                        insert(ProductEntity(items = state.products.products))
                        getLocalProducts().collect { updatedProducts ->
                            insertProducts(updatedProducts)
                        }
                        if (products.isEmpty()) {
                            products.addAll(state.products.products)
                        }
                        isLoading.value = false
                        isSuccess.value = true
                    }
                }
            }
        }
    }

    private fun insert(product: ProductEntity) = viewModelScope.launch {
        productDao.insertProduct(product)
    }

    private fun getLocalProducts(): Flow<List<Product>> {
        return productDao.getAllProducts().map { productEntityList ->
            productEntityList.flatMap { productEntity ->
                productEntity.items
            }
        }
    }

    private fun insertProducts(updatedProducts: List<Product>) {
        products.clear()
        products.addAll(updatedProducts)
        val uniqueProducts = products.distinctBy { it.id }
        products.clear()
        products.addAll(uniqueProducts)
    }
}