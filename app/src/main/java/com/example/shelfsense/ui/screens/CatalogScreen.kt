package com.example.shelfsense.ui.screens

import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.shelfsense.data.AppDatabase
import com.example.shelfsense.data.entities.Product
import com.example.shelfsense.navigation.Routes
import com.example.shelfsense.ui.components.AppScaffold
import com.example.shelfsense.ui.components.BottomNavBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

@Composable
fun CatalogScreen(navController: NavController) {
    val context = LocalContext.current
    val db = remember { AppDatabase.get(context) }
    val dao = remember { db.productDao() }
    val scope = rememberCoroutineScope()

    var query by rememberSaveable { mutableStateOf("") }
    var products by remember { mutableStateOf<List<Product>>(emptyList()) }

    // Load products from Room (no mock data)
    remember(dao) {
        scope.launch(Dispatchers.IO) {
            dao.observeAll()
                .onStart { /* optional loading state */ }
                .collect { list -> products = list }
        }
    }

    val filtered = remember(query, products) {
        if (query.isBlank()) products
        else products.filter {
            it.name.contains(query, ignoreCase = true) ||
                    it.sku.contains(query, ignoreCase = true)
        }
    }

    AppScaffold(
        title = "Catalog",
        bottomBar = { BottomNavBar(navController) },
        onFabClick = { navController.navigate(Routes.ADD_PRODUCT) },
        fabLabel = "Add Product"
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
                Text(
                    text = if (query.isBlank()) "No products yet. Tap “Add Product” to create one."
                    else "No products match “$query”.",
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filtered, key = { it.sku }) { product ->
                        CatalogRow(
                            name = product.name,
                            sku = product.sku,
                            onClick = {
                                // Use the route you already registered in AppNavigation:
                                // composable("product_detail/{code}") { ... }
                                navController.navigate("product_detail/${product.sku}")
                            }
                        )
                        Divider()
                    }
                }
            }
        }
    }
}

@Composable
private fun CatalogRow(name: String, sku: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp)
    ) {
        Text(text = name, style = MaterialTheme.typography.titleMedium)
        Text(text = sku, style = MaterialTheme.typography.bodySmall)
    }
}
