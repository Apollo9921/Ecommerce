package com.example.ecommerce.koin

import com.example.ecommerce.networking.model.Products
import com.example.ecommerce.networking.requests.ProductService
import retrofit2.Response

class ProductRepositoryImpl(
    private val productService: ProductService
): ProductRepository {
    override suspend fun getProducts(
        limit: Int,
        skip: Int
    ): Response<Products> {
        return productService.getProducts(limit, skip)
    }
}