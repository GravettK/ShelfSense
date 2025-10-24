package com.example.shelfsense.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.shelfsense.data.entities.StockItem
import com.example.shelfsense.data.repository.MockData
import com.example.shelfsense.navigation.Routes
import com.example.shelfsense.ui.components.CenteredScreenTitle
import com.example.shelfsense.ui.components.StatusBadge
import com.example.shelfsense.ui.components.BadgeStyle
import com.example.shelfsense.ui.theme.Dimens

@Composable
fun StockScreen(navController: NavController) {
    val items by remember { mutableStateOf(MockData.stock) }

    var searchQuery by remember { mutableStateOf("") }
    val filtered = items.filter {
        it.name.contains(searchQuery, ignoreCase = true) || it.sku.contains(searchQuery, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.ScreenPadding)
    ) {
        CenteredScreenTitle("Stock")
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search") },
            placeholder = { Text("Search by name or SKU") },
            singleLine = true,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        )

        Spacer(Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filtered) { part ->
                StockRow(
                    item = part,
                    onClick = { navController.navigate(Routes.partDetailBySku(part.sku)) }
                )
            }
        }
    }
}

@Composable
private fun StockRow(item: StockItem, onClick: () -> Unit) {
    val isLow = item.onHand < item.minLevel

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.CardPadding),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(item.name, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(4.dp))
                Text("SKU: ${item.sku}", style = MaterialTheme.typography.labelMedium)
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "${item.onHand} pcs",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.height(2.dp))
                StatusBadge(
                    label = if (isLow) "Low stock" else "OK",
                    style  = if (isLow) BadgeStyle.ERROR else BadgeStyle.SUCCESS
                )
            }
        }
    }
}
