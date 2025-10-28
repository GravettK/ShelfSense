package com.example.shelfsense.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.shelfsense.data.entities.OrderEntity
import com.example.shelfsense.data.entities.OrderLineEntity

data class OrderWithLines(
    @Embedded val order: OrderEntity,
    @Relation(parentColumn = "orderNo", entityColumn = "orderNo")
    val lines: List<OrderLineEntity>
)
