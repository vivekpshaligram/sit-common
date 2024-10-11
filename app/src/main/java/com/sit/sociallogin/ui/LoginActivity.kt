package com.sit.sociallogin.ui

import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.sit.common.app.R
import com.sit.common.app.databinding.ActivityLoginBinding
import com.sit.common.base.BaseActivity
import com.sit.common.social.SocialLogin
import com.sit.common.social.loginResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>() {

    override val viewModel: LoginViewModel by viewModels()
    override val layoutId: Int = R.layout.activity_login

    override fun init() {
        printLogManager.printMsg("apiInterface ${viewModel.repository}")

        binding.btnLoginWithGoogle.setOnClickListener {
            googleLoginResultListener.launch(
                SocialLogin.loginIntent(
                    this, SocialLogin.LoginType.GOOGLE
                )
            )
        }

        binding.btnLoginWithFacebook.setOnClickListener {
            facebookLoginResultListener.launch(
                SocialLogin.loginIntent(
                    this, SocialLogin.LoginType.FB
                )
            )
        }

        binding.btnSampleApiCall.setOnClickListener {
            viewModel.getPostListApiCall()
        }
    }

    private val googleLoginResultListener =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val loginResult = result.data?.loginResult
            if (loginResult != null) {
                AlertDialog.Builder(this).setMessage(loginResult.toString())
                    .setTitle("Google Login result").setPositiveButton(android.R.string.ok, null)
                    .apply {
                        if (loginResult.isSuccess) {
                            setNeutralButton("Logout") { _, _ ->
                                SocialLogin.logout(
                                    this@LoginActivity, SocialLogin.LoginType.GOOGLE
                                )
                            }
                        }
                    }.show()
            }
        }

    private val facebookLoginResultListener =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val loginResult = result.data?.loginResult
            if (loginResult != null) {
                AlertDialog.Builder(this).setMessage(loginResult.toString())
                    .setTitle("Facebook Login result").setPositiveButton(android.R.string.ok, null)
                    .apply {
                        if (loginResult.isSuccess) {
                            setNeutralButton("Logout") { _, _ ->
                                SocialLogin.logout(
                                    this@LoginActivity, SocialLogin.LoginType.GOOGLE
                                )
                            }
                        }
                    }.show()
            }
        }
}