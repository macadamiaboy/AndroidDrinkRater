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

    @POST("/reviews")
    fun postData(@Body review: Review): Call<Void>

    @POST("auth/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>
}