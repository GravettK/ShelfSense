package com.example.shelfsense.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.shelfsense.navigation.Routes
import com.example.shelfsense.ui.components.AppScaffold
import com.example.shelfsense.ui.components.BottomNavBar

@Composable
fun OrdersScreen(navController: NavController) {
    AppScaffold(
        title = "Orders",
        onFabClick = { navController.navigate(Routes.ADD_ORDER) },
        fabLabel = "Add Order",
        bottomBar = { BottomNavBar(navController) }
    ) { _: PaddingValues ->
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.TopStart) {
            Text("Orders will appear here.")
        }
    }
}
