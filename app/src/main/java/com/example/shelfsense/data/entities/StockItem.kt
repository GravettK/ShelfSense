package com.example.shelfsense.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stock_items")
data class StockItem(
    @PrimaryKey val sku: String,          // part code or stock identifier
    val name: String,
    val description: String = "",
    val qty: Int = 0,
    val bin: String = "",
    val minQty: Int = 0,                  // for low-stock warnings
    val maxQty: Int = 0,                  // for overstock control
    val updatedAt: Long = System.currentTimeMillis(),
    val synced: Boolean = false           // for offline/online sync tracking
)
