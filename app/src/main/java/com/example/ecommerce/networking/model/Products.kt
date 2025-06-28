package com.example.ecommerce.networking.model

import kotlinx.serialization.Serializable

@Serializable
data class Products(
    val limit: Int,
    val products: List<Product>,
    val skip: Int,
    val total: Int
)