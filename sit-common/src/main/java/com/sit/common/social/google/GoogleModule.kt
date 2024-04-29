package com.sit.common.social.google

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.sit.common.R
import com.sit.common.social.ILoginListener
import com.sit.common.social.ISocialModule
import com.sit.common.social.LoginResult
import com.sit.common.social.SocialLogin

internal class GoogleModule(override val listener: ILoginListener) : ISocialModule {
    override fun login(activity: Activity) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .apply {
                val clientId = activity.getString(R.string.google_default_web_client_id)
                if (clientId.isNotEmpty()) {
                    requestIdToken(clientId)
                } else {
                    Log.w(
                        "SocialLogin",
                        "Can't find R.string.default_web_client_id for request id_token"
                    )
                }
            }
            .requestEmail()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(activity, gso)
        val signInIntent = googleSignInClient.signInIntent
        activity.startActivityForResult(signInIntent, REQUEST_GOOGLE_LOGIN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean =
        when (requestCode) {
            REQUEST_GOOGLE_LOGIN -> {
                var result: LoginResult? = null
                if (data != null) try {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                    val account = task.getResult(ApiException::class.java)!!
                    result = LoginResult(
                        true,
                        SocialLogin.LoginType.GOOGLE,
                        id = account.id,
                        email = account.email,
                        firstName = account.givenName,
                        lastName = account.familyName,
                        token = account.idToken
                    )
                } catch (e: Exception) {
                    Log.w("SocialLogin", "Google sign in failed", e)
                    result = if (e is ApiException) {
                        val message = CommonStatusCodes.getStatusCodeString(e.statusCode)
                        Log.w("SocialLogin", "${e.statusCode} - $message")
                        if (e.statusCode != CommonStatusCodes.CANCELED) {
                            LoginResult(
                                false,
                                SocialLogin.LoginType.GOOGLE,
                                errorMessage = message
                            )
                        } else null
                    } else LoginResult(
                        false,
                        SocialLogin.LoginType.GOOGLE,
                        errorMessage = e.message
                    )

                }
                listener.onLoginResult(result)
                true
            }
            else -> false
        }

    companion object {
        fun logout(context: Context) {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .build()
            val googleSignInClient = GoogleSignIn.getClient(context, gso)
            googleSignInClient.signOut()
        }
    }
}

private const val REQUEST_GOOGLE_LOGIN = 214