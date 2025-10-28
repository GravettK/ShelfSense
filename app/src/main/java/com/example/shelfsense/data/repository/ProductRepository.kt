package com.example.shelfsense.data.repository

import com.example.shelfsense.data.dao.ProductDao
import com.example.shelfsense.data.entities.Product
import kotlinx.coroutines.flow.Flow

class ProductRepository(private val dao: ProductDao) {
    fun observeAll(): Flow<List<Product>> = dao.observeAll()
    suspend fun insert(product: Product) = dao.insert(product)
}
