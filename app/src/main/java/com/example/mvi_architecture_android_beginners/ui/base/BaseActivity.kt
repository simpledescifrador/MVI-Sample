package com.example.mvi_architecture_android_beginners.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity<VM : BaseViewModel> : AppCompatActivity() {
    protected lateinit var viewModel: VM
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = setupViewModel()
    }

    override fun onStart() {
        super.onStart()
        setupUI()
        setupClicks()
        observeViewModel()
    }

    protected abstract fun setupUI()
    protected abstract fun setupViewModel(): VM
    protected abstract fun observeViewModel()
    protected abstract fun setupClicks()

}