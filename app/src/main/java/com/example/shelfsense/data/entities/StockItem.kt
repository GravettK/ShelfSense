package com.example.shelfsense.data.entities

data class StockItem(
    val sku: String,
    val name: String,
    val onHand: Int,
    val minLevel: Int
)
