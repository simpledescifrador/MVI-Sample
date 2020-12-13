package com.example.mvi_architecture_android_beginners.data.repository

import com.example.mvi_architecture_android_beginners.data.network.api.ResultWrapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

abstract class BaseRepository {
    suspend fun <T : Any> safeApiCall(
        dispatcher: CoroutineDispatcher,
        apiCall: suspend () -> ResultWrapper<T>,
    ): ResultWrapper<T> {
        return withContext(dispatcher) {
            try {
                apiCall.invoke()
            } catch (e: Exception) {
                ResultWrapper.NetworkError
            }
        }
    }
}