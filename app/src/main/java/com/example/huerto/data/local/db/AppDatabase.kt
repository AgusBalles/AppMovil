package com.example.huerto.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.huerto.data.local.db.dao.ProductDao
import com.example.huerto.data.local.db.dao.UserDao
import com.example.huerto.data.local.db.entities.ProductEntity
import com.example.huerto.data.local.db.entities.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        com.example.huerto.data.local.db.entities.UserEntity::class,
        com.example.huerto.data.local.db.entities.ProductEntity::class,
        com.example.huerto.data.local.db.entities.CartItemEntity::class // 👈 NUEVA
    ],
    version = 2,               // 👈 SUBE versión
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): com.example.huerto.data.local.db.dao.CartDao
    abstract fun userDao(): com.example.huerto.data.local.db.dao.UserDao
    abstract fun productDao(): com.example.huerto.data.local.db.dao.ProductDao


    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun get(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "huerto.db"
                )
                    .fallbackToDestructiveMigration() // 👈 simple para dev
                    .addCallback(object : Callback() {
                        // (mantén tu precarga de productos si ya la tienes)
                    })
                    .build()
                    .also { INSTANCE = it }
            }
    }
}