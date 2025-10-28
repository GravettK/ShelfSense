package com.example.shelfsense.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.shelfsense.data.entities.OrderEntity
import com.example.shelfsense.data.entities.OrderLineEntity
import com.example.shelfsense.data.relations.OrderWithLines
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {

    // --- Observe ---

    @Transaction
    @Query("SELECT * FROM orders ORDER BY orderNo DESC")
    fun observeOrders(): Flow<List<OrderWithLines>>

    @Transaction
    @Query("SELECT * FROM orders WHERE orderNo = :orderNo LIMIT 1")
    fun observeOrder(orderNo: String): Flow<OrderWithLines?>

    // Optional convenience stream for completed/active filtering can be done in Repo.

    // --- Upserts ---

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertOrder(order: OrderEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertOrders(orders: List<OrderEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertLines(lines: List<OrderLineEntity>)

    // --- Mutations ---

    @Query("UPDATE orders SET status = :status WHERE orderNo = :orderNo")
    suspend fun setStatus(orderNo: String, status: String)

    @Query("DELETE FROM order_lines WHERE orderNo = :orderNo")
    suspend fun deleteLinesForOrder(orderNo: String)

    @Query("DELETE FROM orders WHERE orderNo = :orderNo")
    suspend fun deleteOrder(orderNo: String)

    @Transaction
    suspend fun deleteOrderCompletely(orderNo: String) {
        deleteLinesForOrder(orderNo)
        deleteOrder(orderNo)
    }

    // --- Counts ---

    @Query("SELECT COUNT(*) FROM orders")
    fun countOrders(): Flow<Int>
}
