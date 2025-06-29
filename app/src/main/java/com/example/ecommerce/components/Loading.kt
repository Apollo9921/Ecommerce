package com.example.ecommerce.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ecommerce.core.BackgroundColor
import com.example.ecommerce.core.Black

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = Black,
            strokeWidth = 4.dp
        )
    }
}