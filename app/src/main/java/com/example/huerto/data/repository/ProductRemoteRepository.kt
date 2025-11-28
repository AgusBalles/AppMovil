package com.example.huerto.data.repository



import com.example.huerto.data.api.RetrofitClient
import com.example.huerto.data.local.db.entities.ProductEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductRemoteRepository {

    private val api = RetrofitClient.apiService

    suspend fun getAllProducts(): Result<List<ProductEntity>> = withContext(Dispatchers.IO) {
        try {
            val response = api.getProductos()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProductById(id: Int): Result<ProductEntity> = withContext(Dispatchers.IO) {
        try {
            val response = api.getProductoById(id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createProduct(product: ProductEntity): Result<ProductEntity> = withContext(Dispatchers.IO) {
        try {
            val response = api.createProducto(product)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateProduct(id: Int, product: ProductEntity): Result<ProductEntity> = withContext(Dispatchers.IO) {
        try {
            val response = api.updateProducto(id, product)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteProduct(id: Int): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = api.deleteProducto(id)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun searchProductByName(name: String): Result<List<ProductEntity>> = withContext(Dispatchers.IO) {
        try {
            val response = api.searchProductoByName(name)
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