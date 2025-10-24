package com.example.shelfsense.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.shelfsense.ui.components.CenteredScreenTitle
import com.example.shelfsense.ui.theme.Dimens
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search


data class StockItem(
    val sku: String,
    val name: String,
    val onHand: Int,
    val minLevel: Int
)

@Composable
fun StockScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }

    val allItems = remember {
        listOf(
            StockItem("MTI-001", "Hydraulic Tank Cap", 45, 10),
            StockItem("MTI-002", "Diesel Hose Clamp", 5, 20),
            StockItem("MTI-003", "Aluminium Bracket", 100, 30),
            StockItem("MTI-004", "Seal Ring", 12, 15),
            StockItem("MTI-005", "Pressure Valve", 22, 15)
        )
    }

    // Filter items based on search text
    val filteredItems = remember(searchQuery) {
        allItems.filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
                    it.sku.contains(searchQuery, ignoreCase = true)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(Dimens.ScreenPadding)
    ) {
        CenteredScreenTitle("Stock")

        Spacer(modifier = Modifier.height(8.dp))

        // 🔍 Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            singleLine = true,
            placeholder = { Text("Search by name or SKU") },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                cursorColor = MaterialTheme.colorScheme.primary,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface
            ),

            leadingIcon = {
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Default.Search,
                    contentDescription = "Search",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 📦 Stock list
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredItems) { item ->
                StockItemCard(item = item, onClick = {
                    navController.navigate("component_detail/${item.sku}")
                })
            }

            if (filteredItems.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 50.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No matching items found",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StockItemCard(item: StockItem, onClick: () -> Unit) {
    val cardColor = MaterialTheme.colorScheme.surface
    val textColor = MaterialTheme.colorScheme.onSurface

    val lowStock = item.onHand < item.minLevel
    val accentColor = if (lowStock) MaterialTheme.colorScheme.error
    else MaterialTheme.colorScheme.primary

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.CardPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = textColor
                )
                Text(
                    text = "SKU: ${item.sku}",
                    style = MaterialTheme.typography.labelMedium,
                    color = textColor.copy(alpha = 0.7f)
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${item.onHand} pcs",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = accentColor
                )
                Text(
                    text = if (lowStock) "Low stock" else "OK",
                    style = MaterialTheme.typography.labelMedium,
                    color = accentColor
                )
            }
        }
    }
}
