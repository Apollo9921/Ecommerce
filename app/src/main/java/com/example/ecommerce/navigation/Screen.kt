package com.example.ecommerce.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen {
    @Serializable
    data object Home : Screen

    @Serializable
    data class Details(
        val title: String,
        val thumbnail: String,
        val rating: Double,
        val price: Double,
        val discountPercentage: Double,
        val stock: Int
    ) : Screen
}