package com.example.shelfsense.navigation

data class ScreenConfig(
    val route: String,
    val title: String,
)

object Routes {
    const val HOME = "home"                 // “Orders”
    const val SCAN = "scan"
    const val COMPONENT_DETAIL = "component_detail"
    const val WHERE_USED = "where_used"
    const val LOGIN = "login"
    const val PROFILE = "profile"
}

val ScreenRegistry: Map<String, ScreenConfig> = mapOf(
    Routes.HOME to ScreenConfig(Routes.HOME, "ShelfSense"),
    Routes.SCAN to ScreenConfig(Routes.SCAN, "Scan"),
    Routes.COMPONENT_DETAIL to ScreenConfig(Routes.COMPONENT_DETAIL, "Component Detail"),
    Routes.WHERE_USED to ScreenConfig(Routes.WHERE_USED, "Where Used"),
    Routes.LOGIN to ScreenConfig(Routes.LOGIN, "Login"),
    Routes.PROFILE to ScreenConfig(Routes.PROFILE, "Profile"),
)

// Bottom nav destinations (tabs)
val BottomNavRoutes = listOf(
    Routes.HOME,          // “Orders”
    Routes.WHERE_USED,
    Routes.PROFILE
)

// Helper: show bottom bar on these routes
fun shouldShowBottomBar(route: String?): Boolean =
    route in BottomNavRoutes
