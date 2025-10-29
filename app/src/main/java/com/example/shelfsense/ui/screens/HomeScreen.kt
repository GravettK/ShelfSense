package com.example.shelfsense.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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
    // list of home actions (text + route) so we can render them with LazyColumn
    val actions = listOf(
        "Catalog" to Routes.CATALOG,
        "Stock" to Routes.STOCK,
        "Orders" to Routes.ORDERS,
        "Completed Orders" to Routes.COMPLETED_ORDERS,
        "Add Product" to Routes.ADD_PRODUCT,
        "Add Part" to Routes.ADD_PART,
        "Profile" to Routes.PROFILE,
        "Scan" to Routes.SCAN
    )

    AppScaffold(
        title = "Home",
        bottomBar = { BottomNavBar(navController) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "Welcome, kel",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(Modifier.height(24.dp))
            }

            items(actions) { (label, route) ->
                Button(
                    onClick = { navController.navigate(route) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(label)
                }
            }
        }
    }
}
