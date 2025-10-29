package com.example.shelfsense.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.shelfsense.ui.screens.*

@Composable
fun AppNavigation(
    navController: NavHostController,
    start: String
) {
    NavHost(
        navController = navController,
        startDestination = start
    ) {
        // --- Main tabs
        composable(Routes.HOME)    { HomeScreen(navController) }
        composable(Routes.ORDERS)  { OrdersScreen(navController) }
        composable(Routes.STOCK)   { StockScreen(navController) }
        composable(Routes.CATALOG) { CatalogScreen(navController) }
        composable(Routes.PROFILE) { ProfileScreen(navController) }
        composable(Routes.SCAN)    { ScanScreen(navController) }

        // --- Auth
        composable(Routes.LOGIN)  { LoginScreen(navController) }
        composable(Routes.SIGNUP) { SignupScreen(navController) }

        // --- Adds
        composable(Routes.ADD_ORDER)   { AddOrderScreen(navController) }
        composable(Routes.ADD_PART)    { AddPartScreen(navController) }
        composable(Routes.ADD_PRODUCT) { AddProductScreen(navController) }

        // --- Completed orders (needed for the Home button)
        composable(Routes.COMPLETED_ORDERS) { CompletedOrdersScreen(navController) }

        // --- Part detail by SKU
        composable(
            route = "${Routes.PART_DETAIL_ROOT}/{sku}",
            arguments = listOf(navArgument("sku") { type = NavType.StringType })
        ) { backStack ->
            val sku = backStack.arguments?.getString("sku") ?: ""
            PartDetailScreen(navController, sku)
        }

        // --- Order detail by order number
        composable(
            route = "${Routes.ORDER_DETAIL_ROOT}/{orderNo}",
            arguments = listOf(navArgument("orderNo") { type = NavType.StringType })
        ) { backStack ->
            val orderNo = backStack.arguments?.getString("orderNo") ?: ""
            OrderDetailScreen(navController = navController, orderNo = orderNo)
        }

        // --- Select lists (used by add/edit flows)
        composable("select_part")    { SelectPartScreen(navController) }
        composable("select_product") { SelectProductScreen(navController) }

        // --- Product detail (used by Catalog navigation)
        composable(
            route = "product_detail/{code}",
            arguments = listOf(navArgument("code") { type = NavType.StringType })
        ) { backStack ->
            val code = backStack.arguments?.getString("code")
            ProductDetailScreen(navController = navController, code = code)
        }
    }
}
