package com.example.shelfsense.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.shelfsense.ui.components.ScreenColumn

data class ProductUsage(
    val productName: String,
    val quantityPer: Int
)

@Composable
fun WhereUsedScreen(
    navController: NavController,
    componentName: String? = "Unknown Component"
) {
    // Placeholder list – replace with DAO-backed data when ready
    val usageList = remember {
        listOf(
            ProductUsage("500L Hydraulic Tank", 1),
            ProductUsage("Dual Compartment Tank", 2),
            ProductUsage("Fuel Delivery Assembly", 4),
            ProductUsage("Tank Mounting Frame", 3)
        )
    }

    AppScaffold(
        title = "Where Used",
        bottomBar = { BottomNavBar(navController) }
    ) { pv ->
        // Scroll-ready container that applies Scaffold insets
        ScreenColumn(padding = pv) {

            Text(
                text = "Component: ${componentName ?: "Unknown Component"}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Divider()

            if (usageList.isEmpty()) {
                Text(
                    text = "No product usage data found for this part.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.padding(top = 48.dp)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(usageList) { usage ->
                        ProductUsageCard(usage)
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductUsageCard(usage: ProductUsage) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = usage.productName,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Qty per assembly: ${usage.quantityPer}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
