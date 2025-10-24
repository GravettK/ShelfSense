package com.example.shelfsense.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.shelfsense.navigation.Routes
import com.example.shelfsense.ui.components.CenteredScreenTitle
import com.example.shelfsense.ui.theme.Dimens
import com.example.shelfsense.data.entities.StockItem  // ⬅ use shared model

// Mock data (you can move this later to a repository/ViewModel)
private val mockParts = listOf(
    StockItem("MTI-001", "Hydraulic Tank Cap", 45, 10),
    StockItem("MTI-002", "Diesel Hose Clamp", 5, 20),
    StockItem("MTI-003", "Aluminium Bracket", 100, 30),
    StockItem("MTI-004", "Seal Ring", 12, 15),
    StockItem("MTI-005", "Pressure Valve", 22, 15)
)

@Composable
fun PartDetailScreen(navController: NavController, sku: String?) {
    val part = mockParts.firstOrNull { it.sku == sku }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CenteredScreenTitle("Part Detail")

        if (part == null) {
            Text("Part not found.", style = MaterialTheme.typography.bodyMedium)
        } else {
            Text("SKU: ${part.sku}", style = MaterialTheme.typography.titleMedium)
            Text("Name: ${part.name}", style = MaterialTheme.typography.bodyMedium)
            Text("On Hand: ${part.onHand}", style = MaterialTheme.typography.bodyMedium)
            Text("Minimum Level: ${part.minLevel}", style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { navController.navigate(Routes.whereUsedByName(part.name)) },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) { Text("View Where Used") }
        }
    }
}
