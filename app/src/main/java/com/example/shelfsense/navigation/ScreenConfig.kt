package com.example.shelfsense.navigation

data class ScreenConfig(val route: String, val title: String)

object Routes {
    // Tab routes (bottom nav visible)
    const val HOME = "home"
    const val STOCK = "stock"
    const val PROFILE = "profile"
    const val CATALOG = "catalog"

    // Fullscreen / detail routes (no bottom nav)
    const val SCAN = "scan"
    const val ORDER_DETAIL = "order_detail"
    const val PRODUCT_DETAIL = "product_detail"
    const val PART_DETAIL = "part_detail"
    const val WHERE_USED = "where_used"

    // Add/create screens
    const val ADD_PART = "add_part"
    const val ADD_PRODUCT = "add_product"

    // Args
    const val ARG_ORDER_NO = "orderNo"
    const val ARG_MODEL = "model"
    const val ARG_SKU = "sku"
    const val ARG_NAME = "name"

    // Helpers (URI-encode for safety)
    fun orderDetail(orderNo: String) =
        "$ORDER_DETAIL/${android.net.Uri.encode(orderNo)}"

    fun productDetail(modelCode: String) =
        "$PRODUCT_DETAIL/${android.net.Uri.encode(modelCode)}"

    fun partDetailBySku(sku: String) =
        "$PART_DETAIL/${android.net.Uri.encode(sku)}"

    fun whereUsedByName(componentName: String) =
        "$WHERE_USED/${android.net.Uri.encode(componentName)}"
}

val ScreenRegistry: Map<String, ScreenConfig> = mapOf(
    Routes.HOME to ScreenConfig(Routes.HOME, "ShelfSense"),
    Routes.STOCK to ScreenConfig(Routes.STOCK, "Stock"),
    Routes.CATALOG to ScreenConfig(Routes.CATALOG, "Catalog"),
    Routes.PROFILE to ScreenConfig(Routes.PROFILE, "Profile"),
    Routes.SCAN to ScreenConfig(Routes.SCAN, "Scan"),
    Routes.ORDER_DETAIL to ScreenConfig(Routes.ORDER_DETAIL, "Order Detail"),
    Routes.PRODUCT_DETAIL to ScreenConfig(Routes.PRODUCT_DETAIL, "Product Detail"),
    Routes.PART_DETAIL to ScreenConfig(Routes.PART_DETAIL, "Part Detail"),
    Routes.WHERE_USED to ScreenConfig(Routes.WHERE_USED, "Where Used"),
    Routes.ADD_PART to ScreenConfig(Routes.ADD_PART, "Add Part"),
    Routes.ADD_PRODUCT to ScreenConfig(Routes.ADD_PRODUCT, "Add Product"),
)

val BottomNavRoutes = listOf(Routes.HOME, Routes.STOCK, Routes.CATALOG, Routes.PROFILE)
fun shouldShowBottomBar(route: String?): Boolean = route in BottomNavRoutes
