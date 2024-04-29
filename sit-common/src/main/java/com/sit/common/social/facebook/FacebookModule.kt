package com.sit.common.social.facebook

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.annotation.WorkerThread
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.sit.common.social.ILoginListener
import com.sit.common.social.ISocialModule
import com.sit.common.social.SocialLogin

internal class FacebookModule(
    private val extraPermissions: Array<out String>,
    override val listener: ILoginListener
) : ISocialModule {
    private val fbCallbackManager = CallbackManager.Factory.create()
    private val fbCallback = object : FacebookCallback<LoginResult> {
        override fun onSuccess(result: LoginResult) {
            handleFbLoginToken(result.accessToken)
        }

        override fun onCancel() {
            listener.onLoginResult(null)
        }

        override fun onError(error: FacebookException) {
            listener.onLoginResult(
                com.sit.common.social.LoginResult(
                    false,
                    SocialLogin.LoginType.FB,
                    errorMessage = error.localizedMessage
                )
            )
        }
    }


    override fun login(activity: Activity) {
        FacebookSdk.sdkInitialize(activity)
        val accessToken = AccessToken.getCurrentAccessToken()
        if (accessToken != null && !accessToken.isExpired) {
            handleFbLoginToken(accessToken)
        } else {
            LoginManager.getInstance().registerCallback(fbCallbackManager, fbCallback)
            val permissions = mutableSetOf("public_profile", "email")
            permissions += extraPermissions
            LoginManager.getInstance()
                .logInWithReadPermissions(activity, permissions)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        return fbCallbackManager.onActivityResult(
            requestCode,
            resultCode,
            data
        )
    }

    private fun handleFbLoginToken(token: AccessToken) {
        listener.onLoginResult(
            com.sit.common.social.LoginResult(
                true,
                SocialLogin.LoginType.FB,
                id = token.userId,
                token = token.token
            )
        )
    }

    companion object {
        fun logout() {
            LoginManager.getInstance().logOut()
        }

        @WorkerThread
        fun getWithUserInfo(orig: com.sit.common.social.LoginResult): com.sit.common.social.LoginResult {
            if (!orig.isSuccess) return orig
            val accessToken = AccessToken.getCurrentAccessToken()!!
            val req = GraphRequest.newMeRequest(accessToken, null)
            val parameters = Bundle()
            parameters.putString("fields", "email,first_name,last_name,picture")
            req.parameters = parameters
            val response = req.executeAndWait().jsonObject!!
            val email = response.optString("email").takeIf { it.isNotEmpty() }
            val firstName = response.optString("first_name").takeIf { it.isNotEmpty() }
            val lastName = response.optString("last_name").takeIf { it.isNotEmpty() }
            val avatar = response.optJSONObject("picture")
                ?.optJSONObject("data")
                ?.optString("url")?.takeIf { it.isNotEmpty() }
            return orig.copy(
                email = email,
                firstName = firstName,
                lastName = lastName,
                avatar = avatar
            )
        }
    }
}