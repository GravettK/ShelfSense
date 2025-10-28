package com.example.shelfsense.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stock_items")
data class StockItem(
    @PrimaryKey val sku: String,          // part code
    val name: String,
    val qty: Int = 0,
    val bin: String = "",
    val updatedAt: Long = System.currentTimeMillis()
)
