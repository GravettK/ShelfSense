package com.example.shelfsense.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.shelfsense.ui.screens.AddOrderScreen
import com.example.shelfsense.ui.screens.CatalogScreen
import com.example.shelfsense.ui.screens.HomeScreen
import com.example.shelfsense.ui.screens.OrdersScreen
import com.example.shelfsense.ui.screens.PartDetailScreen
import com.example.shelfsense.ui.screens.ProfileScreen
import com.example.shelfsense.ui.screens.ScanScreen
import com.example.shelfsense.ui.screens.StockScreen

/**
 * Convenience overload for when you aren't inside a Scaffold content lambda.
 * Calls the main AppNavigation with an empty PaddingValues so calls compile.
 */
@Composable
fun AppNavigation(
    navController: NavHostController,
    start: String
) {
    AppNavigation(
        navController = navController,
        start = start,
        padding = PaddingValues()
    )
}


@Composable
fun AppNavigation(
    navController: NavHostController,
    start: String,
    padding: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = start
    ) {
        // Tabs
        composable(Routes.HOME)    { HomeScreen(navController, padding) }
        composable(Routes.ORDERS)  { OrdersScreen(navController, padding) }
        composable(Routes.STOCK)   { StockScreen(navController, padding) }
        composable(Routes.CATALOG) { CatalogScreen(navController, padding) }
        composable(Routes.PROFILE) { ProfileScreen(navController, padding) }
        composable(Routes.SCAN)    { ScanScreen(navController, padding) }

        // Orders
        composable(Routes.ADD_ORDER) {
            AddOrderScreen(navController, padding)
        }

        // Part detail (route argument: sku)
        composable(
            route = "${Routes.PART_DETAIL_ROOT}/{sku}",
            arguments = listOf(navArgument("sku") { type = NavType.StringType })
        ) { backStack ->
            PartDetailScreen(
                navController = navController,
                sku = backStack.arguments?.getString("sku") ?: "",
                padding = padding
            )
        }


    }
}
