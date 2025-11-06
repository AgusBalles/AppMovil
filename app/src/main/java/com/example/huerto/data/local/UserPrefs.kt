package com.example.huerto.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val DS_NAME = "session_prefs"
val Context.dataStore by preferencesDataStore(DS_NAME)

object UserKeys {
    val IS_LOGGED = booleanPreferencesKey("is_logged")
    val EMAIL = stringPreferencesKey("email")
    val NAME = stringPreferencesKey("name")
}

class UserPrefs(private val context: Context) {
    val isLogged: Flow<Boolean> = context.dataStore.data.map { it[UserKeys.IS_LOGGED] ?: false }
    val email: Flow<String> = context.dataStore.data.map { it[UserKeys.EMAIL] ?: "" }
    val name: Flow<String> = context.dataStore.data.map { it[UserKeys.NAME] ?: "" }

    suspend fun saveLogin(email: String, name: String) {
        context.dataStore.edit {
            it[UserKeys.IS_LOGGED] = true
            it[UserKeys.EMAIL] = email
            it[UserKeys.NAME] = name
        }
    }

    suspend fun logout() {
        context.dataStore.edit {
            it[UserKeys.IS_LOGGED] = false
            it[UserKeys.EMAIL] = ""
            it[UserKeys.NAME] = ""
        }
    }
}