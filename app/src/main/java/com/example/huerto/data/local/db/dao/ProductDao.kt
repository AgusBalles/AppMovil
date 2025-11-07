package com.example.huerto.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.huerto.data.local.db.entities.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    // Lista reactiva de productos ordenados por nombre
    @Query("SELECT * FROM products ORDER BY name ASC")
    fun observeAll(): Flow<List<ProductEntity>>

    // Cantidad total de productos (para saber si hay que seedear)
    @Query("SELECT COUNT(*) FROM products")
    suspend fun count(): Int

    // Insertar una lista (usado por seedIfEmpty)
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAll(list: List<ProductEntity>)

    // (Opcional) upsert individual por si lo necesitas
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: ProductEntity)

    // (Opcional) borrar todo (útil en pruebas)
    @Query("DELETE FROM products")
    suspend fun clear()
}