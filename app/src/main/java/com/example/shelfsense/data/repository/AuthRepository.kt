package com.example.shelfsense.repository

import android.content.Context
import android.content.SharedPreferences

object AuthRepository {
    private const val PREFS_NAME = "ShelfSenseAuthPrefs"
    private const val KEY_EMAIL = "email"
    private const val KEY_NAME = "name"
    private const val KEY_PHONE = "phone"
    private const val KEY_LOGGED_IN = "logged_in"
    private const val KEY_TOKEN = "auth_token" // reserved for API use later

    private fun prefs(context: Context): SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun login(context: Context, email: String, name: String = "", phone: String = "", token: String? = null) {
        prefs(context).edit()
            .putString(KEY_EMAIL, email)
            .putString(KEY_NAME, name)
            .putString(KEY_PHONE, phone)
            .putBoolean(KEY_LOGGED_IN, true)
            .apply()

        token?.let { prefs(context).edit().putString(KEY_TOKEN, it).apply() }
    }

    fun logout(context: Context) {
        prefs(context).edit().clear().apply()
    }

    fun isLoggedIn(context: Context): Boolean =
        prefs(context).getBoolean(KEY_LOGGED_IN, false)

    fun getUserEmail(context: Context): String? =
        prefs(context).getString(KEY_EMAIL, null)

    fun getUserName(context: Context): String? =
        prefs(context).getString(KEY_NAME, null)

    fun getUserPhone(context: Context): String? =
        prefs(context).getString(KEY_PHONE, null)

    fun getAuthToken(context: Context): String? =
        prefs(context).getString(KEY_TOKEN, null)
}
