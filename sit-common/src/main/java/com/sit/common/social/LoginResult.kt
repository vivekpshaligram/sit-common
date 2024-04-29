package com.sit.common.social

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginResult(
    val isSuccess: Boolean,
    val loginType: SocialLogin.LoginType,
    val id: String? = null,
    val token: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val avatar: String? = null,
    val email: String? = null,
    var errorMessage: String? = null
) : Parcelable