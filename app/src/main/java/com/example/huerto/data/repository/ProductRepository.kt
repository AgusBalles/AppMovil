package com.example.huerto.data.repository

import android.content.Context
import com.example.huerto.data.local.db.AppDatabase
import com.example.huerto.data.local.db.entities.ProductEntity
import kotlinx.coroutines.flow.Flow

class ProductRepository(context: Context) {
    private val dao = AppDatabase.get(context).productDao()
    private val remoteRepo = ProductRemoteRepository()

    // Observa datos locales (caché)
    fun observeAll(): Flow<List<ProductEntity>> = dao.observeAll()

    // Refresca desde el servidor y actualiza caché
    suspend fun refreshProducts(): Result<Unit> {
        return try {
            val result = remoteRepo.getAllProducts()
            result.fold(
                onSuccess = { products ->
                    dao.deleteAll()
                    dao.insertAll(products)
                    Result.success(Unit)
                },
                onFailure = { error ->
                    Result.failure(error)
                }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Crear producto: primero en servidor, luego en caché
    suspend fun createProduct(product: ProductEntity): Result<ProductEntity> {
        return remoteRepo.createProduct(product).also { result ->
            result.onSuccess { createdProduct ->
                dao.insertAll(listOf(createdProduct))
            }
        }
    }

    // Actualizar producto
    suspend fun updateProduct(id: Int, product: ProductEntity): Result<ProductEntity> {
        return remoteRepo.updateProduct(id, product).also { result ->
            result.onSuccess { updatedProduct ->
                dao.insertAll(listOf(updatedProduct))
            }
        }
    }

    // Eliminar producto
    suspend fun deleteProduct(id: Long): Result<Unit> {
        val result = remoteRepo.deleteProduct(id.toInt())
        result.onSuccess {
            dao.deleteById(id)
        }
        return result
    }

    // Buscar por nombre
    suspend fun searchByName(name: String): Result<List<ProductEntity>> {
        return remoteRepo.searchProductByName(name)
    }

    // Obtener por ID
    suspend fun getProductById(id: Int): Result<ProductEntity> {
        return remoteRepo.getProductById(id)
    }

    // Para inicialización cuando no hay conexión
    suspend fun seedIfEmpty() {
        if (dao.count() == 0) {
            dao.insertAll(
                listOf(
                    ProductEntity(name = "OLA", price = 1200, stock = 15),
                    ProductEntity(name = "platano", price = 900, stock = 20),
                    ProductEntity(name = "zanahoria", price = 700, stock = 30),
                    ProductEntity(name = "pimenton", price = 1500, stock = 12),
                    ProductEntity(name = "quinua", price = 800, stock = 18)
                )
            )
        }
    }

    // Contar productos locales
    suspend fun count(): Int = dao.count()
}