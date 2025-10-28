package com.example.shelfsense.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

/**
 * Accepts an optional [code] (e.g., SKU) coming from NavBackStackEntry args:
 * ProductDetailScreen(navController, backStack.arguments?.getString("code"))
 */
@Composable
fun ProductDetailScreen(
    navController: NavController,
    code: String? = null
) {
    // Pre-fill from nav arg when available; keeps your existing UI intact.
    var name by remember { mutableStateOf("Product") }
    var sku by remember { mutableStateOf(code ?: "SKU-000") }
    var description by remember { mutableStateOf("Description...") }

    Scaffold(topBar = { TopAppBar(title = { Text("Product Details") }) }) { pv ->
        Column(
            modifier = Modifier
                .padding(pv)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("Name: $name")
            Spacer(Modifier.padding(top = 8.dp))
            Text("SKU: $sku")
            Spacer(Modifier.padding(top = 8.dp))
            Text(description)
            Spacer(Modifier.padding(top = 16.dp))
            Button(
                onClick = { /* TODO: actions such as edit, add to stock, etc. */ },
                modifier = Modifier
                    .fillMaxSize(fraction = 1f)
                    .padding(top = 0.dp)
            ) {
                Text("Action")
            }
        }
    }
}
