package com.example.mvi_architecture_android_beginners.ui.main

import android.Manifest.permission
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvi_architecture_android_beginners.data.model.User
import com.example.mvi_architecture_android_beginners.data.network.api.ApiHelperImpl
import com.example.mvi_architecture_android_beginners.data.network.api.RetrofitBuilder
import com.example.mvi_architecture_android_beginners.databinding.ActivityMainBinding
import com.example.mvi_architecture_android_beginners.ui.base.BaseActivity
import com.example.mvi_architecture_android_beginners.ui.main.MainViewModel.MainIntent.*
import com.example.mvi_architecture_android_beginners.ui.main.MainViewModel.MainState.*
import com.example.mvi_architecture_android_beginners.ui.main.adapter.MainAdapter
import com.example.mvi_architecture_android_beginners.ui.main.home.UserDetailFragment
import com.example.mvi_architecture_android_beginners.util.AppUtils
import com.example.mvi_architecture_android_beginners.util.AppUtils.LogType.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@ExperimentalCoroutinesApi
class MainActivity : BaseActivity<MainViewModel>(), MainAdapter.EventListener {

    private var _adapter = MainAdapter(arrayListOf())
    private lateinit var _binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)
    }

    override fun setupClicks() {
        _binding.buttonFetchUser.setOnClickListener {
            lifecycleScope.launch {
                Dexter.withActivity(this@MainActivity)
                    .withPermission(
                        permission.WRITE_EXTERNAL_STORAGE
                    )
                    .withListener(object : PermissionListener {
                        override fun onPermissionGranted(response: PermissionGrantedResponse) {
                            AppUtils.appendLog(
                                this@MainActivity,
                                "Fetch Users",
                                INFO
                            )
                        }

                        override fun onPermissionDenied(response: PermissionDeniedResponse) {

                        }

                        override fun onPermissionRationaleShouldBeShown(
                            permission: PermissionRequest,
                            token: PermissionToken,
                        ) {
                            token.continuePermissionRequest()
                        }
                    }).check()
                viewModel.mainIntent.send(FetchUsers)
            }
        }
        _adapter.setEventListener(this)
    }

    override fun onUserClick(user: User) {
        val fragmentManagerTransaction = supportFragmentManager.beginTransaction()
        fragmentManagerTransaction.replace(_binding.userDetailFrame.id,
            UserDetailFragment.newInstance(user.id))
        fragmentManagerTransaction.commit()
    }

    override fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.mainState.collect {
                when (it) {
                    is Idle -> {
                        //DO NOTHING
                    }
                    is Loading -> {
                        _binding.buttonFetchUser.visibility = View.GONE
                        _binding.progressBar.visibility = View.VISIBLE
                    }
                    is Users -> {
                        _binding.progressBar.visibility = View.GONE
                        _binding.buttonFetchUser.visibility = View.VISIBLE
                        renderUserList(it.user)
                    }
                    is Error -> {
                        _binding.progressBar.visibility = View.GONE
                        _binding.buttonFetchUser.visibility = View.VISIBLE
                        Toast.makeText(this@MainActivity, it.error, Toast.LENGTH_SHORT).show()
                        AppUtils.appendLog(this@MainActivity, it.error, ERROR)
                    }
                }
            }
        }
    }

    private fun renderUserList(users: List<User>) {
        _binding.recyclerView.visibility = View.VISIBLE
        users.let {
            _adapter.addUsers(it)
        }
    }

    override fun setupUI() {
        val recyclerView = _binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.run {
            addItemDecoration(
                DividerItemDecoration(
                    recyclerView.context,
                    (recyclerView.layoutManager as LinearLayoutManager).orientation
                )
            )
        }
        recyclerView.adapter = _adapter
    }

    override fun setupViewModel() = ViewModelProviders.of(
        this,
        MainViewModel.ViewModelFactory(ApiHelperImpl(RetrofitBuilder.apiService))
    ).get(MainViewModel::class.java)


}