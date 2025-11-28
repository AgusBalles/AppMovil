package com.example.huerto.data.local.db.dao
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.huerto.data.local.db.entities.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products")
    fun observeAll(): Flow<List<ProductEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<ProductEntity>)

    @Query("SELECT COUNT(*) FROM products")
    suspend fun count(): Int

    // AGREGAR ESTOS DOS MÉTODOS
    @Query("DELETE FROM products")
    suspend fun deleteAll()

    @Query("DELETE FROM products WHERE id = :id")
    suspend fun deleteById(id: Long)
}