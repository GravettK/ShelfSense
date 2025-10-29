package com.example.shelfsense.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Order line entry belonging to an OrderEntity.
 *
 * lineType:
 *  - "PRODUCT" → itemCode = product SKU
 *  - "PART"    → itemCode = part code
 *  - "CUSTOM"  → free-text description, manual price
 */
@Entity(
    tableName = "order_lines",
    foreignKeys = [
        ForeignKey(
            entity = OrderEntity::class,
            parentColumns = ["orderNo"],
            childColumns = ["orderNo"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("orderNo"),
        Index("lineType"),
        Index("itemCode")
    ]
)
data class OrderLineEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val orderNo: String,
    val lineType: String,           // PRODUCT | PART | CUSTOM
    val itemCode: String? = null,   // SKU, part code, or null if custom
    val description: String = "",   // Product/part name or custom description
    val unitPrice: Double = 0.0,
    val qty: Int = 1,
    val total: Double = unitPrice * qty,
    val synced: Boolean = false,    // for future cloud sync support
    val updatedAt: Long = System.currentTimeMillis()
)
