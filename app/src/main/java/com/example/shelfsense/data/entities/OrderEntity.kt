package com.example.shelfsense.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * status:
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
    val createdAt: Long = System.currentTimeMillis()
)
