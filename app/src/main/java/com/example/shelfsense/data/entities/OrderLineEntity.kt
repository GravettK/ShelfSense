package com.example.shelfsense.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * lineType:
 *  - "PRODUCT" -> itemCode = product SKU/code
 *  - "PART"    -> itemCode = part code
 *  - "CUSTOM"  -> free-text description, manual price
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
    indices = [Index("orderNo"), Index("lineType"), Index("itemCode")]
)
data class OrderLineEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val orderNo: String,
    val lineType: String,          // PRODUCT | PART | CUSTOM
    val itemCode: String? = null,  // product sku or part code when applicable
    val description: String = "",  // human readable text (product/part name or custom text)
    val unitPrice: Double = 0.0,   // resolved from DB for product/part, or entered for custom
    val qty: Int = 1
)
