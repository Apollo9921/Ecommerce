package com.example.ecommerce.room

import androidx.room.TypeConverter
import com.example.ecommerce.networking.model.Product
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class ProductListConverter {

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val productListType = Types.newParameterizedType(List::class.java, Product::class.java)
    private val productListAdapter = moshi.adapter<List<Product>>(productListType)

    @TypeConverter
    fun fromProductList(products: List<Product>?): String? {
        return products?.let { productListAdapter.toJson(it) }
    }

    @TypeConverter
    fun toProductList(productsString: String?): List<Product>? {
        return productsString?.let { productListAdapter.fromJson(it) }
    }
}