package com.example.mvi_architecture_android_beginners.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mvi_architecture_android_beginners.data.model.User
import com.example.mvi_architecture_android_beginners.data.network.api.ApiHelper
import com.example.mvi_architecture_android_beginners.data.network.api.ResultWrapper
import com.example.mvi_architecture_android_beginners.data.repository.MainRepository
import com.example.mvi_architecture_android_beginners.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class MainViewModel(private val repository: MainRepository) : BaseViewModel() {
    class ViewModelFactory(
        private val apiHelper: ApiHelper,
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                return MainViewModel(MainRepository(apiHelper, Dispatchers.IO)) as T
            }
            throw IllegalArgumentException("Unknown class name")
        }

    }

    val mainIntent = Channel<MainIntent>(Channel.UNLIMITED)
    private val _mainState = MutableStateFlow<MainState>(MainState.Idle)
    val mainState: StateFlow<MainState>
        get() = _mainState
    private val _userDetailState = MutableStateFlow<UserDetailState>(UserDetailState.Idle)
    val userDetailState: StateFlow<UserDetailState>
        get() = _userDetailState

    sealed class MainIntent {
        object FetchUsers : MainIntent()
        data class SearchUser(val userId: Int) : MainIntent()
    }

    sealed class UserDetailState {
        object Idle : UserDetailState()
        object Loading : UserDetailState()
        data class UserDetail(val user: User) : UserDetailState()
        data class Error(val error: String?) : UserDetailState()
    }

    sealed class MainState {
        object Idle : MainState()
        object Loading : MainState()
        data class Users(val user: List<User>) : MainState()
        data class Error(val error: String?) : MainState()
    }

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            mainIntent.consumeAsFlow().collect {
                when (it) {
                    is MainIntent.FetchUsers -> fetchUsers()
                    is MainIntent.SearchUser -> searchUser(it.userId)
                }
            }
        }
    }

    private fun searchUser(userId: Int) {
        viewModelScope.launch {
            _userDetailState.value = UserDetailState.Loading
            when (val requestResult = repository.getUserDetail(userId)) {
                is ResultWrapper.Success -> {
                    _userDetailState.value = UserDetailState.UserDetail(requestResult.data)
                }
                is ResultWrapper.NetworkError -> {
                    _mainState.value = MainState.Error("No Network Available")
                }
                is ResultWrapper.Error -> {
                    _mainState.value = MainState.Error(requestResult.exception?.message)
                }
            }
        }
    }

    private fun fetchUsers() {
        viewModelScope.launch {
            _mainState.value = MainState.Loading
            when (val requestResult = repository.getUsers()) {
                is ResultWrapper.Success -> {
                    _mainState.value = MainState.Users(requestResult.data)
                }
                is ResultWrapper.NetworkError -> {
                    _mainState.value = MainState.Error("No Network Available")
                }
                is ResultWrapper.Error -> {
                    _mainState.value = MainState.Error(requestResult.exception?.message)
                }
            }
        }
    }
}