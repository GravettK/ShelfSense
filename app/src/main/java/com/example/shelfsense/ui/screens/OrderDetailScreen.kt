package com.example.shelfsense.ui.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.TaskAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.shelfsense.navigation.Routes
import com.example.shelfsense.ui.components.AppScaffold
import com.example.shelfsense.ui.components.BadgeStyle
import com.example.shelfsense.ui.components.BottomNavBar
import com.example.shelfsense.ui.components.ScreenColumn
import com.example.shelfsense.ui.components.StatusBadge

@Composable
fun OrderDetailScreen(
    navController: NavController,
    orderNo: String
) {
    AppScaffold(
        title = "Order #$orderNo",
        bottomBar = { BottomNavBar(navController) }
    ) { pv ->
        ScreenColumn(padding = pv) {
            Text("Order Details", style = MaterialTheme.typography.titleLarge)

            StatusBadge(label = "Pending", style = BadgeStyle.INFO)

            Divider()

            Surface(
                tonalElevation = 2.dp,
                shape = MaterialTheme.shapes.medium
            ) {
                ScreenColumn(padding = PaddingValues(16.dp)) {
                    Text("Customer: John Doe", style = MaterialTheme.typography.bodyLarge)
                    Text("Order date: 2025-10-29", style = MaterialTheme.typography.bodyMedium)
                    Text("Total items: 3", style = MaterialTheme.typography.bodyMedium)
                    Text("Notes: Urgent delivery", style = MaterialTheme.typography.bodyMedium)
                }
            }

            Divider()

            Text("Actions", style = MaterialTheme.typography.titleMedium)

            Button(onClick = { navController.navigate(Routes.ADD_ORDER) }) {
                Icon(Icons.Outlined.Edit, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Edit Order")
            }

            Button(onClick = { /* mark complete */ }) {
                Icon(Icons.Outlined.TaskAlt, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Complete Order")
            }

            Button(
                onClick = { /* delete */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(Icons.Outlined.Delete, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Delete Order")
            }
        }
    }
}
