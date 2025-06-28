package com.example.ecommerce.networking.requests

import com.example.ecommerce.networking.model.Products
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductService {

    @GET("products")
    suspend fun getProducts(@Query("limit") limit: Int = 20, @Query("skip") skip: Int): Response<Products>

}