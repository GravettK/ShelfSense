package com.example.shelfsense.data.repository

import com.example.shelfsense.data.dao.OrderDao
import com.example.shelfsense.data.entities.OrderEntity
import com.example.shelfsense.data.entities.OrderLineEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OrderRepository(private val dao: OrderDao) {

    // --- Observe (entities) ---

    fun observeAll(): Flow<List<OrderEntity>> =
        dao.observeOrders()

    fun observeActive(): Flow<List<OrderEntity>> =
        observeAll().map { it.filter { o -> o.status != "Completed" } }

    fun observeCompleted(): Flow<List<OrderEntity>> =
        observeAll().map { it.filter { o -> o.status == "Completed" } }

    fun observe(orderNo: String): Flow<OrderEntity?> =
        dao.observeOrder(orderNo)

    // --- Mutations ---

    /** Upsert order; optionally append lines (no overwrite). */
    suspend fun upsert(order: OrderEntity, lines: List<OrderLineEntity> = emptyList()) {
        dao.upsertOrder(order)
        if (lines.isNotEmpty()) dao.upsertLines(lines)
    }

    /** Overwrite all lines for an order. */
    suspend fun replaceLines(orderNo: String, newLines: List<OrderLineEntity>) {
        dao.deleteLinesForOrder(orderNo)
        if (newLines.isNotEmpty()) dao.upsertLines(newLines)
    }

    /** Append additional lines to an existing order. */
    suspend fun addLines(lines: List<OrderLineEntity>) {
        if (lines.isNotEmpty()) dao.upsertLines(lines)
    }

    /** Create or update an order and replace its lines in one call. */
    suspend fun createOrUpdate(order: OrderEntity, lines: List<OrderLineEntity>) {
        dao.upsertOrder(order)
        replaceLines(order.orderNo, lines)
    }

    /** Delete order and all its lines. */
    suspend fun deleteCompletely(orderNo: String) {
        dao.deleteLinesForOrder(orderNo)
        dao.deleteOrder(orderNo)
    }

    // --- Count (derived from stream, since DAO no longer exposes COUNT) ---

    fun count(): Flow<Int> = observeAll().map { it.size }
}
