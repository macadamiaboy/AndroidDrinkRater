package com.example.drinkrater
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("/reviews")
    fun getReviews(@Header("X-Auth-Token") authToken: String): Call<List<Review>>

    @GET("/reviews/{id}")
    fun getReviewById(@Header("X-Auth-Token") authToken: String, @Path("id") id: Int): Call<Review>

    @POST("/reviews")
    fun postData(@Header("X-Auth-Token") authToken: String, @Body review: Review): Call<Void>

    @POST("auth/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("/users")
    fun postUser(@Body userRequest: UserRequest): Call<UserResponse>

    @GET("/users")
    fun getUser(@Header("X-Auth-Token") authToken: String): Call<User>
}