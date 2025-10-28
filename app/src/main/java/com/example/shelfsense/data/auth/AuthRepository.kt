package com.example.shelfsense.data.auth

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthRepository(context: Context) {

    private val helper = AuthDbHelper(context.applicationContext)

    private val _isLoggedIn = MutableStateFlow(helper.isLoggedIn())
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _user = MutableStateFlow(helper.currentUser())
    val user: StateFlow<AuthUser?> = _user

    fun refresh() {
        _isLoggedIn.value = helper.isLoggedIn()
        _user.value = helper.currentUser()
    }

    fun signUp(name: String, email: String, phone: String, password: String): Result<Unit> {
        val res = helper.signUp(name, email, phone, password)
        refresh()
        return res
    }

    fun login(email: String, password: String): Result<Unit> {
        val res = helper.login(email, password)
        refresh()
        return res
    }

    fun updateProfile(name: String, phone: String) {
        helper.updateProfile(name, phone)
        refresh()
    }

    fun logout() {
        helper.logout()
        refresh()
    }
}
