package com.sit.common.utils

import android.util.Log

object PrintLog {

    var enableLog = false

    fun printMsg(msg: Any) {
        if (enableLog) {
            Log.e("Msg ==>", msg.toString())
        }
    }

    fun printMsg(tag: String, msg: String) {
        if (enableLog) {
            Log.e(tag, msg)
        }
    }

    fun printMsg(tag: String, msg: Any) {
        if (enableLog) {
            Log.e(tag, msg.toString())
        }
    }
}