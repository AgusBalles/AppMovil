package com.example.huerto.data.repository

import android.content.Context
import com.example.huerto.data.local.db.dao.CartDao
import com.example.huerto.di.DatabaseProvider
import kotlinx.coroutines.flow.Flow

class CartRepository(context: Context) {
    private val dao: CartDao = DatabaseProvider.db(context).cartDao()

    fun observeCart(email: String) = dao.observeCart(email)
    fun observeCount(email: String): Flow<Int> = dao.observeCount(email)
    fun observeTotal(email: String): Flow<Int> = dao.observeTotal(email)

    suspend fun add(email: String, productId: Long, qty: Int = 1) = dao.add(email, productId, qty)
    suspend fun setQty(email: String, productId: Long, qty: Int) =
        if (qty <= 0) dao.remove(email, productId) else dao.setQuantity(email, productId, qty)

    suspend fun remove(email: String, productId: Long) = dao.remove(email, productId)
    suspend fun clearForUser(email: String) = dao.clearForUser(email)
}