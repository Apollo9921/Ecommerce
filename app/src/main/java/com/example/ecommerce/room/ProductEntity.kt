package com.example.ecommerce.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.ecommerce.networking.model.Product

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val items: List<Product>
)