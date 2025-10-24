package com.example.shelfsense.data.repository

import com.example.shelfsense.data.entities.StockItem

// Domain models
data class Part(val sku: String, val name: String, val qtyPerUnit: Int)
data class ProductModel(
    val code: String,
    val name: String,
    val widthMm: Int,
    val heightMm: Int,
    val lengthMm: Int,
    val weightKg: Int,
    val parts: List<Part> // can be empty (e.g., strap set cut from sheet)
)
data class OrderLine(val modelCode: String, val qty: Int)
enum class OrderStatus { PENDING, IN_PRODUCTION, READY, DELAYED }
enum class Priority { LOW, NORMAL, HIGH }
data class Order(
    val orderNo: String,
    val customer: String,
    val dueDate: String,
    val priority: Priority,
    val status: OrderStatus,
    val lines: List<OrderLine>
)

object MockData {
    // Mutable backing lists so Add screens can append
    private val _stock = mutableListOf(
        StockItem("MTI-001", "Hydraulic Tank Cap", 45, 10),
        StockItem("MTI-002", "Diesel Hose Clamp", 5, 20),
        StockItem("MTI-003", "Aluminium Bracket", 100, 30),
        StockItem("MTI-004", "Seal Ring", 12, 15),
        StockItem("MTI-005", "Pressure Valve", 22, 15),
        StockItem("MTI-006", "Filler Neck", 8, 10),
        StockItem("MTI-007", "1/2\" Fitting", 30, 25),
    )
    private val _products = mutableListOf(
        ProductModel(
            code = "TANK-590-MAN",
            name = "MAN 590L Tank w/ Step",
            widthMm = 650, heightMm = 650, lengthMm = 1200, weightKg = 85,
            parts = listOf(
                Part("MTI-001", "Hydraulic Tank Cap", 1),
                Part("MTI-006", "Filler Neck", 1),
                Part("MTI-007", "1/2\" Fitting", 4),
                Part("MTI-003", "Aluminium Bracket", 2),
            )
        ),
        ProductModel(
            code = "STRAP-950",
            name = "950L Strap Set",
            widthMm = 60, heightMm = 20, lengthMm = 950, weightKg = 5,
            parts = emptyList() // strap set made from sheet; no discrete parts
        )
    )
    private val _orders = mutableListOf(
        Order(
            orderNo = "SO-10231",
            customer = "Acme Water",
            dueDate = "27 Oct",
            priority = Priority.HIGH,
            status = OrderStatus.IN_PRODUCTION,
            lines = listOf(
                OrderLine("TANK-590-MAN", 3),
                OrderLine("STRAP-950", 2)
            )
        ),
        Order(
            orderNo = "SO-10218",
            customer = "BlueRiver Farms",
            dueDate = "25 Oct",
            priority = Priority.HIGH,
            status = OrderStatus.DELAYED,
            lines = listOf(OrderLine("TANK-590-MAN", 2))
        ),
    )

    // Expose read-only views
    val stock: List<StockItem> get() = _stock
    val products: List<ProductModel> get() = _products
    val orders: List<Order> get() = _orders

    // Lookups
    fun getOrder(orderNo: String): Order? = _orders.firstOrNull { it.orderNo == orderNo }
    fun getProduct(modelCode: String): ProductModel? = _products.firstOrNull { it.code == modelCode }
    fun getStock(sku: String): StockItem? = _stock.firstOrNull { it.sku == sku }

    // Mutations (used by Add screens)
    fun addStockItem(item: StockItem) { _stock.add(item) }
    fun addProduct(model: ProductModel) { _products.add(model) }
}
