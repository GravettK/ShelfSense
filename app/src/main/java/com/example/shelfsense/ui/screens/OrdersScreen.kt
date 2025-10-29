package com.example.shelfsense.ui.screens

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Divider
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.shelfsense.navigation.Routes
import com.example.shelfsense.ui.components.AppScaffold
import com.example.shelfsense.ui.components.BadgeStyle
import com.example.shelfsense.ui.components.BottomNavBar
import com.example.shelfsense.ui.components.StatusBadge

@Composable
fun OrdersScreen(navController: NavController) {
    val orders = remember {
        listOf(
            OrderRowUi("ORD-001", "Kelvin", "2025-10-29", "Pending"),
            OrderRowUi("ORD-002", "Workshop", "2025-10-28", "In Progress"),
            OrderRowUi("ORD-003", "John Doe", "2025-10-27", "Completed")
        )
    }

    AppScaffold(
        title = "Orders",
        bottomBar = { BottomNavBar(navController) },
        fab = {
            ExtendedFloatingActionButton(
                text = { Text("Add Order") },
                icon = { Icon(Icons.Outlined.Add, contentDescription = "Add order") },
                onClick = { navController.navigate(Routes.ADD_ORDER) }
            )
        }
    ) { pv ->
        if (orders.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(pv),
                contentAlignment = Alignment.Center
            ) {
                Text("No orders yet.", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = pv,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = orders,
                    key = { it.orderNo } // stable key prevents odd recomposition issues
                ) { order ->
                    OrderRow(
                        order = order,
                        onClick = {
                            // Encode orderNo to be safe, then navigate
                            val encoded = Uri.encode(order.orderNo)
                            navController.navigate("${Routes.ORDER_DETAIL_ROOT}/$encoded")
                        }
                    )
                    Divider()
                }
                item { /* small footer space */ }
            }
        }
    }
}

private data class OrderRowUi(
    val orderNo: String,
    val customer: String,
    val date: String,
    val status: String
)

@Composable
private fun OrderRow(order: OrderRowUi, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text("Order: ${order.orderNo}", style = MaterialTheme.typography.titleMedium)
        Text("Customer: ${order.customer}", style = MaterialTheme.typography.bodyMedium)
        Text("Date: ${order.date}", style = MaterialTheme.typography.bodySmall)
        StatusBadge(
            label = order.status,
            style = when (order.status.lowercase()) {
                "completed" -> BadgeStyle.SUCCESS
                "in progress" -> BadgeStyle.WARNING
                "pending" -> BadgeStyle.INFO
                else -> BadgeStyle.ERROR
            }
        )
    }
}
