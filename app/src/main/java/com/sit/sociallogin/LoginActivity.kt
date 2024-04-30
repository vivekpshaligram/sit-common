package com.sit.sociallogin

import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.sit.common.app.R
import com.sit.common.social.SocialLogin
import com.sit.common.social.loginResult

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

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