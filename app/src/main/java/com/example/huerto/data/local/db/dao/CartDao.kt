package com.example.huerto.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.huerto.data.local.db.entities.CartItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {

    @Query("""
        UPDATE cart_items
        SET quantity = quantity + :qty
        WHERE userEmail = :email AND productId = :productId
    """)
    suspend fun bumpQuantity(email: String, productId: Long, qty: Int): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIgnore(item: CartItemEntity): Long

    @Transaction
    suspend fun add(email: String, productId: Long, qty: Int = 1) {
        val updated = bumpQuantity(email, productId, qty)
        if (updated == 0) {
            insertIgnore(CartItemEntity(userEmail = email, productId = productId, quantity = qty))
        }
    }

    @Query("UPDATE cart_items SET quantity = :qty WHERE userEmail = :email AND productId = :productId")
    suspend fun setQuantity(email: String, productId: Long, qty: Int)

    @Query("DELETE FROM cart_items WHERE userEmail = :email AND productId = :productId")
    suspend fun remove(email: String, productId: Long)

    @Query("DELETE FROM cart_items WHERE userEmail = :email")
    suspend fun clearForUser(email: String)

    @Query("""
        SELECT ci.id AS id,
               ci.productId AS productId,
               p.name AS name,
               p.price AS price,
               ci.quantity AS quantity,
               (p.price * ci.quantity) AS subtotal
        FROM cart_items ci
        JOIN products p ON p.id = ci.productId
        WHERE ci.userEmail = :email
        ORDER BY p.name ASC
    """)
    fun observeCart(email: String): Flow<List<CartLine>>

    @Query("SELECT COALESCE(SUM(quantity), 0) FROM cart_items WHERE userEmail = :email")
    fun observeCount(email: String): Flow<Int>

    @Query("""
        SELECT COALESCE(SUM(p.price * ci.quantity), 0)
        FROM cart_items ci
        JOIN products p ON p.id = ci.productId
        WHERE ci.userEmail = :email
    """)
    fun observeTotal(email: String): Flow<Int>
}