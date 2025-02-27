package com.sit.common.ui

import androidx.activity.viewModels
import com.sit.common.app.R
import com.sit.common.app.databinding.ActivityLoginBinding
import com.sit.common.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>() {

    override val viewModel: LoginViewModel by viewModels()
    override val layoutId: Int = R.layout.activity_login

    override fun init() {
        printLogManager.printMsg("apiInterface ${viewModel.repository}")

        binding.btnSampleApiCall.setOnClickListener {
            viewModel.getPostListApiCall()
        }
    }
}