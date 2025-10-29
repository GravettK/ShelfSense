package com.example.shelfsense.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.shelfsense.data.entities.OrderEntity
import com.example.shelfsense.data.entities.OrderLineEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Query("SELECT * FROM orders ORDER BY orderNo DESC")
    fun observeOrders(): Flow<List<OrderEntity>>

    @Query("SELECT * FROM orders WHERE orderNo = :orderNo LIMIT 1")
    fun observeOrder(orderNo: String): Flow<OrderEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertOrder(order: OrderEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertLines(lines: List<OrderLineEntity>)

    @Query("DELETE FROM order_lines WHERE orderNo = :orderNo")
    suspend fun deleteLinesForOrder(orderNo: String)

    @Query("DELETE FROM orders WHERE orderNo = :orderNo")
    suspend fun deleteOrder(orderNo: String)
}
