package com.example.drinkrater

data class Review(
    val id: Int,
    val name: String,
    val rating: Int,
    val price: Int,
    val description: String,
    val producer: String,
    val abv: Float,
    val userId: Int
)

