package com.example.ecommerce.networking.model

import android.os.Parcelable
import kotlinx.serialization.Serializable
import kotlinx.parcelize.Parcelize

@Serializable
@Parcelize
data class Meta(
    val barcode: String,
    val createdAt: String,
    val qrCode: String,
    val updatedAt: String
) : Parcelable