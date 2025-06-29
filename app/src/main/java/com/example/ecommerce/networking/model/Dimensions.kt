package com.example.ecommerce.networking.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Dimensions(
    val depth: Double,
    val height: Double,
    val width: Double
): Parcelable