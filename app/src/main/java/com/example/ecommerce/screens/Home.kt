package com.example.ecommerce.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import com.example.ecommerce.R
import com.example.ecommerce.components.ErrorScreen
import com.example.ecommerce.components.LoadingScreen
import com.example.ecommerce.components.TopBar
import com.example.ecommerce.core.BackgroundColor
import com.example.ecommerce.core.ProductColor
import com.example.ecommerce.core.RatingColor
import com.example.ecommerce.core.Typography
import com.example.ecommerce.core.White
import com.example.ecommerce.networking.model.Product
import com.example.ecommerce.networking.viewModel.ProductViewModel
import com.example.ecommerce.utils.network.ConnectivityObserver
import com.example.ecommerce.utils.size.ScreenSizeUtils
import org.koin.androidx.compose.koinViewModel

private var viewModel: ProductViewModel? = null
private var isConnected = mutableStateOf(false)

@Composable
fun HomeScreen(navController: NavController) {
    viewModel = koinViewModel<ProductViewModel>()
    val networkStatus = viewModel?.networkStatus?.collectAsState()
    if (networkStatus?.value == ConnectivityObserver.Status.Available && !isConnected.value) {
        isConnected.value = true
        viewModel?.getProducts()
    }

    val isLoading = viewModel?.isLoading?.value
    val isSuccess = viewModel?.isSuccess?.value
    val isError = viewModel?.isError?.value
    val errorMessage = viewModel?.errorMessage?.value
    val products = viewModel?.products

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding(),
        topBar = { TopBar(title = stringResource(R.string.home), isBack = false, backStack = {}) },
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BackgroundColor)
                    .padding(it)
            ) {
                when {
                    isSuccess == true -> {
                        ProductList(
                            products = products
                        )
                    }

                    isLoading == true -> {
                        LoadingScreen()
                    }

                    isError == true -> {
                        ErrorScreen(errorMessage = errorMessage)
                    }

                    networkStatus?.value == ConnectivityObserver.Status.Unavailable -> {
                        ErrorScreen(errorMessage = stringResource(R.string.no_internet_connection))
                    }
                }
            }
        }
    )
}

@Composable
private fun ProductList(products: MutableList<Product>?) {
    val imageLoadingStates = remember { mutableStateMapOf<String, AsyncImagePainter.State>() }
    var allImagesLoaded by remember { mutableStateOf(false) }

    val lazyGridState = rememberLazyGridState()

    LaunchedEffect(imageLoadingStates.toMap(), products) {
        allImagesLoaded = if (products?.isNotEmpty() == true) {
            products.all { product ->
                val imageUrl = product.thumbnail
                imageLoadingStates[imageUrl] is AsyncImagePainter.State.Success || imageLoadingStates[imageUrl] is AsyncImagePainter.State.Error
            }
        } else {
            true
        }
    }

    LazyVerticalGrid(
        state = lazyGridState,
        columns = GridCells.Fixed(2)
    ) {
        items(products?.size ?: 0) { index ->
            val product = products?.get(index)
            val title = product?.title
            var imageUrl: String? = product?.thumbnail
            val rating = product?.rating

            val titleSize = ScreenSizeUtils.calculateCustomWidth(baseSize = 15).sp
            val ratingSize = ScreenSizeUtils.calculateCustomWidth(baseSize = 15).sp
            val height = ScreenSizeUtils.calculateCustomWidth(baseSize = 300).dp

            Box(
                modifier = Modifier
                    .wrapContentWidth()
                    .height(height)
                    .padding(10.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(ProductColor),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            style = Typography.titleLarge.copy(fontSize = ratingSize),
                            text = rating.toString(),
                            color = White,
                            fontSize = ratingSize
                        )
                        Spacer(Modifier.padding(5.dp))
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = RatingColor
                        )
                    }
                    AsyncImage(
                        model = imageUrl,
                        placeholder = painterResource(R.drawable.ic_launcher_background),
                        error = painterResource(R.drawable.ic_launcher_background),
                        onError = { state ->
                            imageLoadingStates[imageUrl!!] = state
                        },
                        onSuccess = { state ->
                            imageLoadingStates[imageUrl!!] = state
                        },
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .aspectRatio(1f)
                            .padding(10.dp)
                    )
                    Text(
                        style = Typography.labelMedium.copy(fontSize = titleSize),
                        text = title ?: "",
                        fontSize = titleSize
                    )
                }
            }

            if (products?.isNotEmpty() == true) {
                if (index == products.size - 1 && allImagesLoaded) {
                    viewModel?.getProducts()
                }
            }
        }
    }
}