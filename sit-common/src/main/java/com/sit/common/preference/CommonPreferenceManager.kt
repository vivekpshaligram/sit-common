package com.sit.common.preference

import android.content.Context
import android.content.SharedPreferences
import com.sit.common.ext.convertToJson
import com.sit.common.ext.convertToList
import com.sit.common.ext.convertToModel
import com.sit.common.ext.isNotNullOrEmpty
import com.sit.common.utils.Constant
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class CommonPreferenceManager @Inject constructor(@ApplicationContext context: Context) {

    companion object {
        const val AUTH_TOKEN = "AUTH_TOKEN"
    }

    val pref: SharedPreferences by lazy {
        context.getSharedPreferences(Constant.SIT_COMMON, Context.MODE_PRIVATE)
    }

    private val editor: SharedPreferences.Editor = pref.edit()

    var authToken: String
        get() = getDataByKey(AUTH_TOKEN, "")
        set(fcm) = pref.edit().putString(AUTH_TOKEN, fcm).apply()

    inline fun <reified T> setPref(key: String, value: T) {
        val editor = pref.edit()
        when (value) {
            is Boolean -> editor.putBoolean(key, (value as Boolean?)!!)
            is String -> editor.putString(key, value as String?)
            is Float -> editor.putFloat(key, (value as Float?)!!)
            is Long -> editor.putLong(key, (value as Long?)!!)
            is Int -> editor.putInt(key, (value as Int?)!!)
        }
        editor.apply()
    }

    inline fun <reified T> getPref(key: String, defaultValue: T? = null): T? {
        return when (defaultValue) {
            is Boolean -> {
                pref.getBoolean(key, defaultValue) as? T
            }

            is String -> {
                val ret = pref.getString(key, defaultValue as String?)
                ret as T?
            }

            is Float -> {
                val result = pref.getFloat(key, (defaultValue as Float?)!!)
                result as T?
            }

            is Long -> {
                val result = pref.getLong(key, (defaultValue as Long?)!!)
                result as T?
            }

            is Int -> {
                val result = pref.getInt(key, (defaultValue as Int?)!!)
                result as T?
            }

            else -> {
                return null
            }
        }
    }

    inline fun <reified T> setJsonPref(key: String, value: T) {
        val editor = pref.edit()
        editor.putString(key, convertToJson(value))
        editor.apply()
    }

    inline fun <reified T> getArrayJsonPref(key: String): T? {
        val getValue = getDataByKey(key)
        return if (getValue.isNotNullOrEmpty()) {
            convertToList(getValue)
        } else {
            null
        }
    }

    inline fun <reified T> getModelJsonPref(key: String, value: T): T? {
        val getValue = getDataByKey(key)
        return if (getValue.isNotNullOrEmpty()) {
            convertToModel(getValue)
        } else {
            null
        }
    }

    fun removePref(key: String) {
        pref.edit().remove(key).apply()
    }

    fun removeAllPrefs() {
        pref.edit().clear().apply()
    }

    @JvmOverloads
    fun getDataByKey(key: String, defaultValue: String = ""): String {
        return if (pref.contains(key)) {
            pref.getString(key, defaultValue).toString()
        } else {
            defaultValue
        }
    }
}