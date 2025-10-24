package com.example.shelfsense.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.shelfsense.ui.components.AppScaffold
import com.example.shelfsense.ui.components.BottomNavBar
import com.example.shelfsense.ui.screens.*

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route ?: Routes.HOME
    val cfg = ScreenRegistry[currentRoute] ?: ScreenRegistry[Routes.HOME]!!
    val showBottom = shouldShowBottomBar(currentRoute)

    // Top bar "Add" action only for Stock and Catalog
    val topBarActions: (@Composable () -> Unit)? = when (currentRoute) {
        Routes.STOCK -> {
            { IconButton(onClick = { navController.navigate(Routes.ADD_PART) }) {
                Icon(Icons.Default.Add, contentDescription = "Add part")
            } }
        }
        Routes.CATALOG -> {
            { IconButton(onClick = { navController.navigate(Routes.ADD_PRODUCT) }) {
                Icon(Icons.Default.Add, contentDescription = "Add product")
            } }
        }
        else -> null
    }

    AppScaffold(
        title = cfg.title,
        onFabClick = { if (currentRoute != Routes.SCAN) navController.navigate(Routes.SCAN) },
        fabLabel = "Scan",
        topBarActions = topBarActions,
        bottomBar = if (showBottom) { { BottomNavBar(navController) } } else null
    ) { modifier ->
        NavHost(
            navController = navController,
            startDestination = Routes.HOME,
            modifier = modifier
        ) {
            // Tabs
            composable(Routes.HOME)    { HomeScreen(navController) }
            composable(Routes.STOCK)   { StockScreen(navController) }
            composable(Routes.CATALOG) { CatalogScreen(navController) }
            composable(Routes.PROFILE) { ProfileScreen(navController) }

            // Scan
            composable(Routes.SCAN)    { ScanScreen(navController) }

            // Detail
            composable(
                route = "${Routes.ORDER_DETAIL}/{${Routes.ARG_ORDER_NO}}",
                arguments = listOf(navArgument(Routes.ARG_ORDER_NO) { type = NavType.StringType })
            ) { backStackEntry ->
                OrderDetailScreen(navController, backStackEntry.arguments?.getString(Routes.ARG_ORDER_NO))
            }

            composable(
                route = "${Routes.PRODUCT_DETAIL}/{${Routes.ARG_MODEL}}",
                arguments = listOf(navArgument(Routes.ARG_MODEL) { type = NavType.StringType })
            ) { backStackEntry ->
                ProductDetailScreen(navController, modelCode = backStackEntry.arguments?.getString(Routes.ARG_MODEL))
            }

            composable(
                route = "${Routes.PART_DETAIL}/{${Routes.ARG_SKU}}",
                arguments = listOf(navArgument(Routes.ARG_SKU) { type = NavType.StringType })
            ) { backStackEntry ->
                PartDetailScreen(navController, backStackEntry.arguments?.getString(Routes.ARG_SKU))
            }

            composable(
                route = "${Routes.WHERE_USED}/{${Routes.ARG_NAME}}",
                arguments = listOf(navArgument(Routes.ARG_NAME) { type = NavType.StringType })
            ) { backStackEntry ->
                WhereUsedScreen(navController, componentName = backStackEntry.arguments?.getString(Routes.ARG_NAME))
            }

            // Create
            composable(Routes.ADD_PART)    { AddPartScreen(navController) }
            composable(Routes.ADD_PRODUCT) { AddProductScreen(navController) }
        }
    }
}
