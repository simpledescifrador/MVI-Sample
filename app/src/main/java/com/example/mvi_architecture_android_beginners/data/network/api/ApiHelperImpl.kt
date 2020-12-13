package com.example.mvi_architecture_android_beginners.data.network.api

import com.example.mvi_architecture_android_beginners.data.model.User
import java.io.IOException

class ApiHelperImpl(private val apiService: ApiService) : ApiHelper {
    override suspend fun getUsers(): ResultWrapper<List<User>> {
        val response = apiService.getUsersAsync()
        return try {
            if (response.isSuccessful) {
                ResultWrapper.Success(response.body()!!)
            } else {
                ResultWrapper.Error(IOException("Error occurred during fetching users"))
            }
        } catch (e: Exception) {
            ResultWrapper.Error(IOException("Unable to fetch users"))
        }
    }

    override suspend fun getUserDetail(userId: Int): ResultWrapper<User> {
        val response = apiService.getUserDetailAsync(userId)
        return try {
            if (response.isSuccessful) {
                ResultWrapper.Success(response.body()!!)
            } else {
                ResultWrapper.Error(IOException("Error occurred during fetching user detail"))
            }
        } catch (e: Exception) {
            ResultWrapper.Error(IOException("Unable to fetch user detail"))
        }

    }

}