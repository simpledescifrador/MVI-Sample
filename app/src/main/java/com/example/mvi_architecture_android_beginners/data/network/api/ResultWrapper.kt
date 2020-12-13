package com.example.mvi_architecture_android_beginners.data.network.api

sealed class ResultWrapper<out T : Any> {
    data class Success<out T : Any>(val data: T) : ResultWrapper<T>()
    data class Error(val exception: Exception?) : ResultWrapper<Nothing>()
    object NetworkError : ResultWrapper<Nothing>()
}
