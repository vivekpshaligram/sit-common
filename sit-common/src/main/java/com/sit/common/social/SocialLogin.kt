package com.sit.common.social

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.annotation.WorkerThread
import com.sit.common.social.facebook.FacebookModule
import com.sit.common.social.google.GoogleModule

class SocialLogin : Activity(), ILoginListener {

    private lateinit var loginType: LoginType
    private lateinit var socialModule: ISocialModule

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginType = intent.loginType!!
        val permissions = intent.permissions!!
        socialModule = when (loginType) {
            LoginType.GOOGLE -> GoogleModule(this)
            LoginType.FB -> FacebookModule(permissions, this)
        }
        if (savedInstanceState == null) {
            socialModule.login(this)
        }
    }

    override fun onLoginResult(result: LoginResult?) {
        val intent = Intent()
        intent.loginResult = result
        setResult(RESULT_OK, intent)
        finish()
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (!socialModule.onActivityResult(requestCode, resultCode, data)) finish()
    }

    enum class LoginType {
        GOOGLE,
        FB
    }

    companion object {
        fun loginIntent(context: Context, loginType: LoginType, vararg extraPermissions: String) =
            Intent(context, SocialLogin::class.java)
                .also {
                    it.loginType = loginType
                    it.permissions = extraPermissions
                }

        fun logout(context: Context, loginType: LoginType) {
            try {
                when (loginType) {
                    LoginType.FB -> FacebookModule.logout()
                    LoginType.GOOGLE -> GoogleModule.logout(context)
                    else -> Log.w("SocialLogin", "Unsupported logout for $loginType")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        @WorkerThread
        fun getResultWithUserInfo(orig: LoginResult): LoginResult {
            return when (orig.loginType) {
                LoginType.GOOGLE -> orig
                LoginType.FB -> FacebookModule.getWithUserInfo(orig)
                else -> {
                    Log.w("SocialLogin", "Unsupported user info for ${orig.loginType}")
                    orig
                }
            }
        }
    }
}