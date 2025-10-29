package com.example.shelfsense.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.PlaylistAdd
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.shelfsense.navigation.Routes
import com.example.shelfsense.ui.components.AppScaffold
import com.example.shelfsense.ui.components.BottomNavBar
import com.example.shelfsense.ui.components.BadgeStyle
import com.example.shelfsense.ui.components.StatusBadge

/**
 * Accepts an optional [code] (SKU) from NavBackStackEntry arguments.
 * Example route: "${Routes.PRODUCT_DETAIL}/{code}"
 */
@Composable
fun ProductDetailScreen(
    navController: NavController,
    code: String? = null
) {
    // Placeholder state until you bind to DAO/repository
    var name by remember { mutableStateOf("Product") }
    var sku by remember { mutableStateOf(code ?: "SKU-000") }
    var description by remember { mutableStateOf("No description provided.") }
    var price by remember { mutableStateOf(0.0) }
    var active by remember { mutableStateOf(true) }

    AppScaffold(
        title = "Product • $sku",
        bottomBar = { BottomNavBar(navController) }
    ) { innerPadding ->
        val scroll = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(scroll),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = "Product Details", style = MaterialTheme.typography.titleLarge)

            StatusBadge(
                label = if (active) "Active" else "Inactive",
                style = if (active) BadgeStyle.SUCCESS else BadgeStyle.WARNING
            )

            Divider()

            Surface(
                tonalElevation = 2.dp,
                shape = MaterialTheme.shapes.medium
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    InfoRow("Name", name)
                    InfoRow("SKU", sku)
                    InfoRow("Price", if (price > 0.0) "R $price" else "—")
                    Text("Description", style = MaterialTheme.typography.titleSmall)
                    Text(description, style = MaterialTheme.typography.bodyMedium)
                }
            }

            Divider()

            Text("Actions", style = MaterialTheme.typography.titleMedium)

            Button(
                onClick = { navController.navigate(Routes.ADD_PRODUCT) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Outlined.Edit, contentDescription = "Edit")
                Spacer(Modifier.width(8.dp))
                Text("Edit Product")
            }

            Button(
                onClick = { navController.navigate(Routes.ADD_ORDER) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Outlined.PlaylistAdd, contentDescription = "Add to order")
                Spacer(Modifier.width(8.dp))
                Text("Add to Order")
            }

            Button(
                onClick = { /* TODO: delete product (confirm → DAO → popBackStack) */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(Icons.Outlined.Delete, contentDescription = "Delete")
                Spacer(Modifier.width(8.dp))
                Text("Delete Product")
            }

            Spacer(Modifier.height(24.dp)) // bottom breathing room
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Column {
        Text(label, style = MaterialTheme.typography.titleSmall)
        Text(value, style = MaterialTheme.typography.bodyLarge)
    }
}
