package com.example.mvi_architecture_android_beginners.data.network.api

import com.example.mvi_architecture_android_beginners.data.model.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("users")
    suspend fun getUsersAsync(): Response<List<User>>

    @GET("users/{user_id}")
    suspend fun getUserDetailAsync(@Path("user_id") userId: Int): Response<User>

}