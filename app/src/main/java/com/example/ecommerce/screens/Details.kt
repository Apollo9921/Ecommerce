package com.example.ecommerce.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import com.example.ecommerce.components.TopBar
import com.example.ecommerce.core.BackgroundColor
import com.example.ecommerce.core.Black
import com.example.ecommerce.core.OutOfStockColor
import com.example.ecommerce.core.ProductColor
import com.example.ecommerce.core.PriceBackgroundColor
import com.example.ecommerce.core.RatingColor
import com.example.ecommerce.core.StockColor
import com.example.ecommerce.core.Typography
import com.example.ecommerce.core.White
import com.example.ecommerce.utils.size.ScreenSizeUtils

@Composable
fun DetailsScreen(
    title: String,
    thumbnail: String,
    rating: Double,
    price: Double,
    discountPercentage: Double,
    stock: Int,
    backStack: () -> Boolean
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding(),
        topBar = {
            TopBar(
                title = title,
                isBack = true,
                backStack = { backStack() }
            )
        },
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BackgroundColor)
                    .padding(it)
            ) {
                DetailsContent(
                    title = title,
                    thumbnail = thumbnail,
                    rating = rating,
                    price = price,
                    discountPercentage = discountPercentage,
                    stock = stock
                )
            }
        }
    )
}

@Composable
private fun DetailsContent(
    title: String,
    thumbnail: String,
    rating: Double,
    price: Double,
    discountPercentage: Double,
    stock: Int
) {
    val scrollState = rememberScrollState()
    val parallaxFactor = 0.5f
    val height = ScreenSizeUtils.calculateCustomWidth(baseSize = 250).dp
    val textSize = ScreenSizeUtils.calculateCustomWidth(baseSize = 20).sp
    val ratingSize = ScreenSizeUtils.calculateCustomWidth(baseSize = 20).sp
    val priceSize = ScreenSizeUtils.calculateCustomWidth(baseSize = 35).sp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .background(White)
                .clipToBounds(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(data = thumbnail)
                        .build()
                ),
                contentDescription = title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .aspectRatio(1f)
                    .graphicsLayer {
                        translationY = scrollState.value * parallaxFactor
                    }
            )
        }

        Column(
            modifier = Modifier
                .wrapContentSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    style = Typography.titleLarge.copy(fontSize = textSize),
                    text = title,
                    color = Black,
                    textAlign = TextAlign.Start
                )
            }
            Spacer(Modifier.padding(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                val stockText = if (stock > 0) "In Stock ($stock)" else "Out of Stock"
                val stockIcon = if (stock > 0) Icons.Default.Check else Icons.Default.Close
                val stockColor = if (stock > 0) StockColor else OutOfStockColor
                Icon(
                    imageVector = stockIcon,
                    contentDescription = null,
                    tint = stockColor
                )
                Text(
                    text = stockText,
                    style = Typography.displayMedium.copy(fontSize = textSize),
                    color = Black
                )
                Spacer(Modifier.padding(10.dp))
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = RatingColor
                )
                Spacer(Modifier.padding(5.dp))
                Text(
                    style = Typography.displayMedium.copy(fontSize = ratingSize),
                    text = rating.toString(),
                    color = Black,
                    fontSize = ratingSize
                )
            }
            Spacer(Modifier.padding(10.dp))
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .clip(RoundedCornerShape(10.dp))
                    .background(PriceBackgroundColor)
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        style = Typography.displayMedium.copy(fontSize = priceSize),
                        text = "Price: $${price}",
                        color = Black
                    )
                    Spacer(Modifier.padding(10.dp))
                    Box(
                        modifier = Modifier
                            .wrapContentSize()
                            .clip(RoundedCornerShape(10.dp))
                            .background(ProductColor)
                            .padding(10.dp)
                    ) {
                        Text(
                            style = Typography.displayMedium.copy(fontSize = textSize),
                            text = "Discount of ${discountPercentage.toInt()}%"
                        )
                    }
                }
            }
        }
    }
}