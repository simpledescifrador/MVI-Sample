package com.example.mvi_architecture_android_beginners.data.repository

import com.example.mvi_architecture_android_beginners.data.model.User
import com.example.mvi_architecture_android_beginners.data.network.api.ApiHelper
import com.example.mvi_architecture_android_beginners.data.network.api.ResultWrapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class MainRepository(
    private val apiHelper: ApiHelper,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseRepository() {

    suspend fun getUsers(): ResultWrapper<List<User>> =
        safeApiCall(dispatcher, apiCall = { apiHelper.getUsers() })

    suspend fun getUserDetail(userId: Int): ResultWrapper<User> =
        safeApiCall(dispatcher, apiCall = { apiHelper.getUserDetail(userId) })
}