package com.example.shelfsense.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.shelfsense.data.entities.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Query("SELECT * FROM products ORDER BY name ASC")
    fun observeAll(): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE name LIKE '%' || :query || '%' OR sku LIKE '%' || :query || '%' ORDER BY name COLLATE NOCASE")
    fun search(query: String): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE sku = :sku LIMIT 1")
    fun getBySku(sku: String): Flow<Product?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: Product)

    @Query("DELETE FROM products WHERE sku = :sku")
    suspend fun deleteBySku(sku: String)
}
