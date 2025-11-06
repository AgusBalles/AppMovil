package com.example.huerto.data.repository

import com.example.huerto.data.local.UserPrefs

import kotlinx.coroutines.flow.Flow

class SessionRepository(private val prefs: UserPrefs) {
    val isLogged: Flow<Boolean> = prefs.isLogged
    val email: Flow<String> = prefs.email
    val name: Flow<String> = prefs.name

    suspend fun login(email: String, name: String) = prefs.saveLogin(email, name)
    suspend fun logout() = prefs.logout()
}