package com.example.shelfsense.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.shelfsense.data.dao.StockItemDao
import com.example.shelfsense.data.dao.ProductDao
import com.example.shelfsense.data.dao.OrderDao
import com.example.shelfsense.data.entities.StockItem
import com.example.shelfsense.data.entities.Product
import com.example.shelfsense.data.entities.OrderEntity
import com.example.shelfsense.data.entities.OrderLineEntity

@Database(
    entities = [
        StockItem::class,
        Product::class,
        OrderEntity::class,
        OrderLineEntity::class
    ],
    version = 2,               // bump to force rebuild if v1 existed
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun stockItemDao(): StockItemDao
    abstract fun productDao(): ProductDao
    abstract fun orderDao(): OrderDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun get(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "shelfsense_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
