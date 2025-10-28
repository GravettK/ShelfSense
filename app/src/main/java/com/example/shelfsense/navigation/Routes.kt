package com.example.shelfsense.navigation

object Routes {
    // Main tabs
    const val HOME = "home"
    const val ORDERS = "orders"
    const val STOCK = "stock"
    const val CATALOG = "catalog"
    const val PROFILE = "profile"

    // Scanner tab
    const val SCAN = "scan"

    // Order add (keep if you already had; used by FAB on Orders)
    const val ADD_ORDER = "add_order"

    // Part detail deep-link by SKU
    const val PART_DETAIL_ROOT = "part_detail"
    fun partDetail(sku: String) = "$PART_DETAIL_ROOT/$sku"
}
