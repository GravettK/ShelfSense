package com.example.shelfsense.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a sales or production order.
 * Status options:
 *  - Pending
 *  - In Progress
 *  - On Hold
 *  - Completed
 *  - Cancelled
 */
@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey val orderNo: String,
    val customer: String,
    val status: String = "Pending",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val synced: Boolean = false // for offline/online sync tracking
)
