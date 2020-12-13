package com.example.mvi_architecture_android_beginners.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.example.mvi_architecture_android_beginners.R
import com.example.mvi_architecture_android_beginners.data.network.api.ApiHelperImpl
import com.example.mvi_architecture_android_beginners.data.network.api.RetrofitBuilder
import com.example.mvi_architecture_android_beginners.databinding.FragmentUserBinding
import com.example.mvi_architecture_android_beginners.ui.base.BaseFragment
import com.example.mvi_architecture_android_beginners.ui.main.MainViewModel
import com.example.mvi_architecture_android_beginners.util.AppUtils
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

private const val USER_ID = "user_id"

@ExperimentalCoroutinesApi
class UserDetailFragment : BaseFragment<MainViewModel>() {

    private var userId: Int = 0
    private lateinit var _binding: FragmentUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userId = it.getInt(USER_ID)
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(userId: Int) =
            UserDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(USER_ID, userId)
                }
            }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentUserBinding.inflate(inflater)
        return _binding.root
    }

    override fun setupUI() {

    }

    override fun setupViewModel() = ViewModelProviders.of(
        this,
        MainViewModel.ViewModelFactory(ApiHelperImpl(RetrofitBuilder.apiService))
    ).get(MainViewModel::class.java)

    override fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.userDetailState.collect { state ->
                when (state) {
                    is MainViewModel.UserDetailState.Idle -> {

                    }
                    is MainViewModel.UserDetailState.Loading -> {
                        _binding.textViewHomeName.text = getString(R.string.loading_text)
                        _binding.textViewHomeEmail.text = getString(R.string.loading_text)
                    }
                    is MainViewModel.UserDetailState.UserDetail -> {
                        _binding.textViewHomeName.text = state.user.name
                        _binding.textViewHomeEmail.text = state.user.email
                    }
                    is MainViewModel.UserDetailState.Error -> {
                        Toast.makeText(context, state.error, Toast.LENGTH_SHORT).show()
                        context?.let { it ->
                            AppUtils.appendLog(it,
                                state.error,
                                AppUtils.LogType.ERROR)
                        }
                    }
                }
            }
        }
    }

    override fun setupClicks() {
        lifecycleScope.launch {
            viewModel.mainIntent.send(MainViewModel.MainIntent.SearchUser(userId))
        }
    }
}