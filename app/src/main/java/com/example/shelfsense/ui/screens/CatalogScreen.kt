package com.example.shelfsense.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun CatalogScreen(
    navController: NavController,
    padding: PaddingValues,
) {
    var q by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(padding)
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = q,
            onValueChange = { q = it },
            label = { Text("Search products") },
            modifier = Modifier.fillMaxWidth()
        )
        // TODO: show product list (similar to Stock)
    }
}
