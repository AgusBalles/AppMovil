package com.example.huerto.data.repository


import android.content.Context
import com.example.huerto.data.local.db.AppDatabase
import com.example.huerto.data.local.db.entities.ProductEntity
import kotlinx.coroutines.flow.Flow


class ProductRepository(context: Context) {
    private val dao = AppDatabase.get(context).productDao()

    fun observeAll(): Flow<List<ProductEntity>> = dao.observeAll()
    suspend fun insertAll(vararg items: ProductEntity) = dao.insertAll(items.toList())
    suspend fun count(): Int = dao.count()

    suspend fun seedIfEmpty() {
        if (dao.count() == 0) {
            dao.insertAll(
                listOf(
                    ProductEntity(name = "espinaca",    price = 1200, stock = 15),
                    ProductEntity(name = "platano",   price =  900, stock = 20),
                    ProductEntity(name = "zanahoria", price =  700, stock = 30),
                    ProductEntity(name = "pimenton",  price = 1500, stock = 12),
                    ProductEntity(name = "quinua",  price =  800, stock = 18),
                )
            )
        }
    }
}