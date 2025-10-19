package com.example.shelfsense.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.shelfsense.ui.components.AppScaffold
import com.example.shelfsense.ui.components.BottomNavBar
import com.example.shelfsense.ui.screens.*

@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
        ?: Routes.HOME
    val cfg = ScreenRegistry[currentRoute] ?: ScreenRegistry[Routes.HOME]!!

    val showBottomBar = shouldShowBottomBar(currentRoute)

    AppScaffold(
        title = cfg.title,
        onFabClick = {
            if (currentRoute != Routes.SCAN) navController.navigate(Routes.SCAN)
        },
        fabLabel = "Scan",
        bottomBar = if (showBottomBar) {
            { BottomNavBar(navController) }
        } else null,
        topBarActions = {
            // Add global actions later, e.g. Sync, Profile, etc.
        }
    ) { modifier ->
        NavHost(
            navController = navController,
            startDestination = Routes.HOME,
            modifier = modifier
        ) {
            composable(Routes.HOME) { HomeScreen(navController) }         // Orders
            composable(Routes.SCAN) { ScanScreen(navController) }         // Big center FAB targets this
            composable(Routes.COMPONENT_DETAIL) { ComponentDetailScreen(navController) }
            composable(Routes.WHERE_USED) { WhereUsedScreen(navController) }
            composable(Routes.LOGIN) { LoginScreen(navController) }
            composable(Routes.PROFILE) { ProfileScreen(navController) }
        }
    }
}
