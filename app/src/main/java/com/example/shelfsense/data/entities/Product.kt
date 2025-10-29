package com.example.shelfsense.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey val sku: String,          // product code / SKU
    val name: String,
    val description: String = "",
    val cost: Double = 0.0,
    val category: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val synced: Boolean = false           // for future online/offline sync
)
