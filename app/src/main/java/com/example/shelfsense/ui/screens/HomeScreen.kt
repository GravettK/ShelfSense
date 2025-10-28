package com.example.shelfsense.ui.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.shelfsense.navigation.Routes
import com.example.shelfsense.ui.components.AppScaffold
import com.example.shelfsense.ui.components.BottomNavBar

@Composable
fun HomeScreen(navController: NavController) {
    AppScaffold(
        title = "Home",
        bottomBar = { BottomNavBar(navController) }
    ) { pv: PaddingValues ->
        Column(
            modifier = Modifier
                .padding(pv)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text("Welcome, kel")
            Spacer(Modifier.height(16.dp))

            @Composable
            fun BigButton(text: String, route: String) {
                Button(
                    onClick = { navController.navigate(route) },
                    modifier = Modifier
                        .fillMaxSize()
                        .height(48.dp)
                ) { Text(text) }
            }

            BigButton("Catalog", Routes.CATALOG)
            Spacer(Modifier.height(12.dp))
            BigButton("Stock", Routes.STOCK)
            Spacer(Modifier.height(12.dp))
            BigButton("Orders", Routes.ORDERS)
            Spacer(Modifier.height(12.dp))
            BigButton("Add Product", Routes.CATALOG)
            Spacer(Modifier.height(12.dp))
            BigButton("Add Part", Routes.STOCK)
            Spacer(Modifier.height(12.dp))
            BigButton("Profile", Routes.PROFILE)
            Spacer(Modifier.height(12.dp))
            BigButton("Scan", Routes.SCAN)
        }
    }
}
