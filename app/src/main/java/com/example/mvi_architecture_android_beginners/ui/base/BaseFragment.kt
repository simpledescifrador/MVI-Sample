package com.example.mvi_architecture_android_beginners.ui.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

abstract class BaseFragment<VM : BaseViewModel> : Fragment() {
    protected lateinit var viewModel: VM

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = setupViewModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupClicks()
        observeViewModel()
    }

    protected abstract fun setupUI()
    protected abstract fun setupViewModel(): VM
    protected abstract fun observeViewModel()
    protected abstract fun setupClicks()

}