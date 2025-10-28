package com.example.shelfsense.repository

import android.content.Context
import android.content.SharedPreferences

object AuthRepository {
    private const val PREFS_NAME = "ShelfSenseAuthPrefs"
    private const val KEY_EMAIL = "email"
    private const val KEY_LOGGED_IN = "logged_in"

    private fun prefs(context: Context): SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun login(context: Context, email: String) {
        prefs(context).edit()
            .putString(KEY_EMAIL, email)
            .putBoolean(KEY_LOGGED_IN, true)
            .apply()
    }

    fun logout(context: Context) {
        prefs(context).edit()
            .clear()
            .apply()
    }

    fun isLoggedIn(context: Context): Boolean =
        prefs(context).getBoolean(KEY_LOGGED_IN, false)

    fun getUserEmail(context: Context): String? =
        prefs(context).getString(KEY_EMAIL, null)
}
