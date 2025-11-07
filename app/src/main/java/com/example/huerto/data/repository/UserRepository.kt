package com.example.huerto.data.repository

import android.content.Context
import com.example.huerto.data.local.db.entities.UserEntity
import com.example.huerto.di.DatabaseProvider

class UserRepository(private val context: Context) {
    private val userDao = DatabaseProvider.db(context).userDao()

    suspend fun register(email: String, name: String, password: String): Result<Long> =
        runCatching { userDao.insert(UserEntity(email = email, name = name, password = password)) }

    suspend fun login(email: String, password: String) = userDao.login(email, password)
    suspend fun findByEmail(email: String) = userDao.findByEmail(email)
}