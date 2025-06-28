package com.example.ecommerce.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    data object Details : Screen("details/{productId}") {
        fun createRoute(productId: String) = "details/$productId"
    }
}