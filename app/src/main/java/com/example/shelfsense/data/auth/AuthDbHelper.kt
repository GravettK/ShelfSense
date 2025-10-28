package com.example.shelfsense.data.auth

import android.content.Context
import android.content.Context.MODE_PRIVATE
import java.security.MessageDigest

data class AuthUser(
    val name: String,
    val email: String,
    val phone: String
)

class AuthDbHelper(private val context: Context) {

    private val prefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE)

    fun isLoggedIn(): Boolean = prefs.getBoolean(KEY_LOGGED_IN, false)

    fun currentUser(): AuthUser? {
        val email = prefs.getString(KEY_EMAIL, null) ?: return null
        val name = prefs.getString(KEY_NAME, "") ?: ""
        val phone = prefs.getString(KEY_PHONE, "") ?: ""
        return AuthUser(name = name, email = email, phone = phone)
    }

    fun signUp(name: String, email: String, phone: String, password: String): Result<Unit> {
        // Single-user store; if an account exists with different email, overwrite intentionally.
        val hash = sha256(password)
        prefs.edit()
            .putString(KEY_NAME, name)
            .putString(KEY_EMAIL, email)
            .putString(KEY_PHONE, phone)
            .putString(KEY_PW_HASH, hash)
            .putBoolean(KEY_LOGGED_IN, true)
            .apply()
        return Result.success(Unit)
    }

    fun login(email: String, password: String): Result<Unit> {
        val storedEmail = prefs.getString(KEY_EMAIL, null) ?: return Result.failure(IllegalStateException("No account found. Please sign up."))
        val storedHash = prefs.getString(KEY_PW_HASH, null) ?: return Result.failure(IllegalStateException("No account found. Please sign up."))
        val tryHash = sha256(password)
        if (email.equals(storedEmail, ignoreCase = true) && tryHash == storedHash) {
            prefs.edit().putBoolean(KEY_LOGGED_IN, true).apply()
            return Result.success(Unit)
        }
        return Result.failure(IllegalArgumentException("Invalid credentials"))
    }

    fun updateProfile(name: String, phone: String) {
        prefs.edit()
            .putString(KEY_NAME, name)
            .putString(KEY_PHONE, phone)
            .apply()
    }

    fun logout() {
        prefs.edit().putBoolean(KEY_LOGGED_IN, false).apply()
    }

    private fun sha256(input: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    companion object {
        private const val PREFS_NAME = "shelfsense_auth_prefs"
        private const val KEY_LOGGED_IN = "logged_in"
        private const val KEY_NAME = "name"
        private const val KEY_EMAIL = "email"
        private const val KEY_PHONE = "phone"
        private const val KEY_PW_HASH = "pw_hash"
    }
}
