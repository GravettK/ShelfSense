package com.example.shelfsense.data.repository

import com.example.shelfsense.data.dao.StockItemDao
import com.example.shelfsense.data.entities.StockItem
import kotlinx.coroutines.flow.Flow

class StockRepository(private val dao: StockItemDao) {
    fun observeAll(): Flow<List<StockItem>> = dao.observeAll()
    suspend fun insert(item: StockItem) = dao.insert(item)
}
