package com.example.huerto.di

import android.content.Context
import com.example.huerto.data.local.db.AppDatabase

object DatabaseProvider {
    fun db(context: Context) = AppDatabase.get(context)
}