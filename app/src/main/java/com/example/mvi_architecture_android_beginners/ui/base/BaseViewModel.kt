package com.example.mvi_architecture_android_beginners.ui.base

import androidx.lifecycle.ViewModel
import com.example.mvi_architecture_android_beginners.data.network.api.ResultWrapper
import kotlinx.coroutines.*
import java.io.IOException

abstract class BaseViewModel : ViewModel() {
    protected val viewModelScope = CoroutineScope(Dispatchers.IO + Job())
}