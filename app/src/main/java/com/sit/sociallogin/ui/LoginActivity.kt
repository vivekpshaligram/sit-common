package com.sit.sociallogin.ui

import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.sit.common.app.R
import com.sit.common.social.SocialLogin
import com.sit.common.social.loginResult
import com.sit.common.utils.PrintLog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        PrintLog.printMsg("apiInterface ${viewModel.repository}")

        findViewById<AppCompatButton>(R.id.btnLoginWithGoogle).setOnClickListener {
            googleLoginResultListener.launch(
                SocialLogin.loginIntent(
                    this, SocialLogin.LoginType.GOOGLE
                )
            )
        }

        findViewById<AppCompatButton>(R.id.btnLoginWithFacebook).setOnClickListener {
            facebookLoginResultListener.launch(
                SocialLogin.loginIntent(
                    this, SocialLogin.LoginType.FB
                )
            )
        }

        findViewById<AppCompatButton>(R.id.btnSampleApiCall).setOnClickListener {
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