package com.example.shelfsense.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.shelfsense.ui.components.AppScaffold
import com.example.shelfsense.ui.components.BottomNavBar

@Composable
fun SelectProductScreen(
    navController: NavController
) {
    var query by remember { mutableStateOf("") }

    // Demo data (replace with DAO later)
    val products = remember {
        listOf(
            ProductRowUi("SKU-001", "Widget A"),
            ProductRowUi("SKU-002", "Widget B"),
            ProductRowUi("SKU-003", "Widget C"),
        )
    }

    val filtered = products.filter {
        query.isBlank() ||
                it.name.contains(query, ignoreCase = true) ||
                it.sku.contains(query, ignoreCase = true)
    }

    AppScaffold(
        title = "Select Product",
        bottomBar = { BottomNavBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Search products") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            if (filtered.isEmpty()) {
                Text("No matching products.", style = MaterialTheme.typography.bodyMedium)
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filtered) { row ->
                        TextButton(
                            onClick = {
                                // Return the SKU to the caller
                                navController.previousBackStackEntry
                                    ?.savedStateHandle
                                    ?.set("selected_product_sku", row.sku)
                                navController.popBackStack()
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Text(row.name, style = MaterialTheme.typography.titleMedium)
                                Text(row.sku, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                        Divider()
                    }
                }
            }
        }
    }
}

private data class ProductRowUi(
    val sku: String,
    val name: String
)
