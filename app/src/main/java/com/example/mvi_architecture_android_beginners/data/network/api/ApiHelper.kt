package com.example.mvi_architecture_android_beginners.data.network.api

import com.example.mvi_architecture_android_beginners.data.model.User

interface ApiHelper {
    suspend fun getUsers(): ResultWrapper<List<User>>
    suspend fun getUserDetail(userId: Int): ResultWrapper<User>
}