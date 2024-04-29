package com.sit.common.social

import android.content.Intent

var Intent.loginType: SocialLogin.LoginType?
    set(value) {
        putExtra("loginType", value?.ordinal ?: -1)
    }
    get() = getIntExtra("loginType", -1)
        .takeIf { it != -1 }?.let { SocialLogin.LoginType.entries[it] }

var Intent.permissions: Array<out String>?
    set(value) {
        putExtra("permissions", value)
    }
    get() = getStringArrayExtra("permissions")

var Intent.loginResult: LoginResult?
    set(value) {
        putExtra("loginResult", value)
    }
    get() = getParcelableExtra("loginResult")