package com.example.shelfsense.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.shelfsense.ui.components.AppScaffold
import com.example.shelfsense.ui.components.BottomNavBar

@Composable
fun CompletedOrdersScreen(navController: NavController) {
    // Placeholder data; replace with DAO-backed list when connected to DB
    val completedOrders = remember {
        listOf(
            CompletedOrderRowUi("ORD-0001", "kel", "2025-10-28 16:20"),
            CompletedOrderRowUi("ORD-0002", "shop floor", "2025-10-28 17:05"),
        )
    }

    AppScaffold(
        title = "Completed Orders",
        bottomBar = { BottomNavBar(navController) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (completedOrders.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            "No completed orders yet.",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            "Completed orders will appear here once they’re finished.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            } else {
                items(completedOrders) { row ->
                    CompletedOrderRow(row)
                    Divider()
                }
            }
        }
    }
}

private data class CompletedOrderRowUi(
    val orderNo: String,
    val customer: String,
    val completedAt: String
)

@Composable
private fun CompletedOrderRow(row: CompletedOrderRowUi) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = row.orderNo,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "Customer: ${row.customer}",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "Completed: ${row.completedAt}",
            style = MaterialTheme.typography.bodySmall
        )
    }
}
