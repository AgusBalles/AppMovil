package com.example.huerto.data.repository

import com.example.huerto.data.api.RetrofitClient
import com.example.huerto.data.local.db.entities.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRemoteRepository {

    private val api = RetrofitClient.apiService

    suspend fun getAllUsers(): Result<List<UserEntity>> = withContext(Dispatchers.IO) {
        try {
            val response = api.getUsuarios()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserById(id: Long): Result<UserEntity> = withContext(Dispatchers.IO) {
        try {
            val response = api.getUsuarioById(id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserByEmail(email: String): Result<UserEntity> = withContext(Dispatchers.IO) {
        try {
            val response = api.getUsuarioByEmail(email)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createUser(user: UserEntity): Result<UserEntity> = withContext(Dispatchers.IO) {
        try {
            val response = api.createUsuario(user)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateUser(id: Long, user: UserEntity): Result<UserEntity> = withContext(Dispatchers.IO) {
        try {
            val response = api.updateUsuario(id, user)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteUser(id: Long): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = api.deleteUsuario(id)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun existsEmail(email: String): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            val response = api.existeEmail(email)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}