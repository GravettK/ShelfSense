package com.example.shelfsense.data.repository

import com.example.shelfsense.data.dao.OrderDao
import com.example.shelfsense.data.entities.OrderEntity
import com.example.shelfsense.data.entities.OrderLineEntity
import com.example.shelfsense.data.relations.OrderWithLines
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OrderRepository(private val dao: OrderDao) {

    /** All orders with lines */
    fun observeAll(): Flow<List<OrderWithLines>> =
        dao.observeOrders()

    /** Active = anything not Completed */
    fun observeActive(): Flow<List<OrderWithLines>> =
        observeAll().map { list -> list.filter { it.order.status != "Completed" } }

    /** Completed only */
    fun observeCompleted(): Flow<List<OrderWithLines>> =
        observeAll().map { list -> list.filter { it.order.status == "Completed" } }

    /** Single order by orderNo */
    fun observe(orderNo: String): Flow<OrderWithLines?> =
        dao.observeOrder(orderNo)

    /** Upsert order + (optional) lines */
    suspend fun upsert(order: OrderEntity, lines: List<OrderLineEntity>) {
        dao.upsertOrder(order)
        if (lines.isNotEmpty()) dao.upsertLines(lines)
    }

    /** Change status */
    suspend fun setStatus(orderNo: String, status: String) =
        dao.setStatus(orderNo, status)

    /** Delete order and its lines */
    suspend fun deleteCompletely(orderNo: String) =
        dao.deleteOrderCompletely(orderNo)

    /** Count */
    fun count(): Flow<Int> = dao.countOrders()
}
