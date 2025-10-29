package com.example.shelfsense.navigation

object Routes {
    // Auth
    const val LOGIN = "login"
    const val SIGNUP = "signup"

    // Main tabs
    const val HOME = "home"
    const val ORDERS = "orders"
    const val STOCK = "stock"
    const val CATALOG = "catalog"
    const val PROFILE = "profile"

    // Scanner
    const val SCAN = "scan"

    // Extras
    const val COMPLETED_ORDERS = "completed_orders"
    const val ADD_PRODUCT = "add_product"
    const val ADD_PART = "add_part"
    const val ADD_ORDER = "add_order"

    // Part detail
    const val PART_DETAIL_ROOT = "part_detail"
    fun partDetail(sku: String) = "$PART_DETAIL_ROOT/$sku"

    // Order detail
    const val ORDER_DETAIL_ROOT = "order_detail"
    fun orderDetail(orderNo: String) = "$ORDER_DETAIL_ROOT/$orderNo"

    // Product detail  (new, minimal)
    const val PRODUCT_DETAIL_ROOT = "product_detail"
    fun productDetail(code: String) = "$PRODUCT_DETAIL_ROOT/$code"


}
