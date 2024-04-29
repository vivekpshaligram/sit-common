package com.sit.common.social

import android.app.Activity
import android.content.Intent

internal interface ISocialModule {
    val listener: ILoginListener
    fun login(activity: Activity)
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean
}

interface ILoginListener {
    fun onLoginResult(result: LoginResult?)
}