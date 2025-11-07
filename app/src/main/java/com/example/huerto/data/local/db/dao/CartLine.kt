package com.example.huerto.data.local.db.dao

data class CartLine(
    val id: Long,
    val productId: Long,
    val name: String,
    val price: Int,
    val quantity: Int,
    val subtotal: Int
)
