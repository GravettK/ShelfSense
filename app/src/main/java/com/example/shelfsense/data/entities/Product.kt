package com.example.shelfsense.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey val sku: String,          // product code
    val name: String,
    val cost: Double = 0.0,
    val createdAt: Long = System.currentTimeMillis()
)
