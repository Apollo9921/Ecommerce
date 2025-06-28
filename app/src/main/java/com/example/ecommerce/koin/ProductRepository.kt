package com.example.ecommerce.koin

import com.example.ecommerce.networking.model.Products
import retrofit2.Response

interface ProductRepository {
    suspend fun getProducts(limit: Int, skip: Int): Response<Products>
}