package com.example.drinkrater

import com.google.gson.annotations.SerializedName

data class Review(
    val id: Int? = null,
    val name: String,
    val rating: Int,
    val price: Int,
    val description: String,
    val producer: String,
    val abv: Float,
    @SerializedName("user_id") val userId: Int
)

