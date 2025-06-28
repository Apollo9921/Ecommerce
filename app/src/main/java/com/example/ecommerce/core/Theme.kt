package com.example.ecommerce.core

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun EcommerceTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        typography = Typography,
        content = content
    )
}