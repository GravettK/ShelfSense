
package com.example.shelfsense.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.shelfsense.ui.screens.HomeScreen
import com.example.shelfsense.ui.screens.ScanScreen
import com.example.shelfsense.ui.screens.ComponentDetailScreen
import com.example.shelfsense.ui.screens.WhereUsedScreen
import com.example.shelfsense.ui.screens.LoginScreen
import com.example.shelfsense.ui.screens.ProfileScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Scan : Screen("scan")
    object ComponentDetail : Screen("component_detail")
    object WhereUsed : Screen("where_used")
    object Login : Screen("login")
    object Profile : Screen("profile")
}

@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) { HomeScreen(navController) }
        composable(Screen.Scan.route) { ScanScreen(navController) }
        composable(Screen.ComponentDetail.route) { ComponentDetailScreen(navController) }
        composable(Screen.WhereUsed.route) { WhereUsedScreen(navController) }
        composable(Screen.Login.route) { LoginScreen(navController) }
        composable(Screen.Profile.route) { ProfileScreen(navController) }
    }
}
