package com.example.shelfsense.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.example.shelfsense.data.entities.StockItem
import kotlinx.coroutines.flow.Flow

@Dao
interface StockItemDao {

    @Query("SELECT * FROM stock_items ORDER BY name COLLATE NOCASE ASC")
    fun observeAll(): Flow<List<StockItem>>

    @Query("SELECT * FROM stock_items WHERE sku = :sku LIMIT 1")
    fun observeBySku(sku: String): Flow<StockItem?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: StockItem)

    @Update
    suspend fun update(item: StockItem)

    @Delete
    suspend fun delete(item: StockItem)

    @Query("DELETE FROM stock_items WHERE sku = :sku")
    suspend fun deleteBySku(sku: String)
}
