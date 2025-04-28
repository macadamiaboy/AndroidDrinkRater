package com.example.drinkrater
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("/reviews")
    fun getReviews(): Call<List<Review>>

    @GET("/reviews/{id}")
    fun getReviewById(@Path("id") id: Int): Call<Review>

    //@POST("/your_endpoint")
    //fun postData(@Body data: YourDataClass): Call<YourDataClass>
}