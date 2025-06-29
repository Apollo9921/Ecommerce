package com.example.ecommerce.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.ecommerce.components.TopBar

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

        }
    )
}