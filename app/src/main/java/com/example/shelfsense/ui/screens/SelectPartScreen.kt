package com.example.shelfsense.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.shelfsense.ui.components.AppScaffold
import com.example.shelfsense.ui.components.BottomNavBar

@Composable
fun SelectPartScreen(
    navController: NavController
) {
    // Demo parts (replace with DAO/Room)
    val parts = remember {
        listOf(
            PartRowUi("P-0001", "3mm 3CR12 Sheet"),
            PartRowUi("P-0002", "M8x20 Hex Bolt"),
            PartRowUi("P-0003", "Aluminium Angle 40x40"),
        )
    }

    AppScaffold(
        title = "Select Part",
        bottomBar = { BottomNavBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (parts.isEmpty()) {
                Text("No parts found.", style = MaterialTheme.typography.bodyMedium)
            } else {
                parts.forEachIndexed { idx, row ->
                    TextButton(
                        onClick = {
                            navController.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set("selected_part_sku", row.sku)
                            navController.popBackStack()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(row.name, style = MaterialTheme.typography.titleMedium)
                            Text(row.sku, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                    if (idx != parts.lastIndex) Divider()
                }
            }
        }
    }
}

private data class PartRowUi(
    val sku: String,
    val name: String
)
