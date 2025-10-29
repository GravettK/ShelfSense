package com.example.shelfsense.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.layout.PaddingValues
import com.example.shelfsense.ui.components.AppScaffold
import com.example.shelfsense.ui.components.BottomNavBar
import com.example.shelfsense.ui.components.BadgeStyle
import com.example.shelfsense.ui.components.StatusBadge

@Composable
fun PartDetailScreen(
    navController: NavController,
    sku: String,
    padding: PaddingValues = PaddingValues()
) {
    var currentQty by rememberSaveable(sku) { mutableStateOf<Int?>(null) }
    var bin by rememberSaveable(sku) { mutableStateOf<String?>(null) }
    var delta by remember { mutableStateOf("") }

    AppScaffold(
        title = "Part • $sku",
        bottomBar = { BottomNavBar(navController) }
    ) { innerPadding ->
        val scroll = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(scroll),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text("Part Details", style = MaterialTheme.typography.titleLarge)

            StatusBadge(
                label = if ((currentQty ?: 0) > 0) "In Stock" else "Out of Stock",
                style = if ((currentQty ?: 0) > 0) BadgeStyle.SUCCESS else BadgeStyle.ERROR
            )

            Divider()

            InfoRow("SKU", sku)
            InfoRow("Quantity", currentQty?.toString() ?: "—")
            InfoRow("Bin / Location", bin ?: "—")

            Spacer(Modifier.height(8.dp))

            Text("Adjust Quantity", style = MaterialTheme.typography.titleMedium)

            OutlinedTextField(
                value = delta,
                onValueChange = { s ->
                    val cleaned = s.trim()
                        .replace("\\s".toRegex(), "")
                        .let { txt ->
                            when {
                                txt.isEmpty() -> ""
                                txt == "-" -> "-"
                                txt.matches(Regex("-?\\d+")) -> txt
                                else -> delta
                            }
                        }
                    delta = cleaned
                },
                label = { Text("Change by (e.g. 5 or -3)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { /* will call adjust via buttons below */ }
                )
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        val v = delta.toIntOrNull()
                        if (v == null || v <= 0) {
                            Toast.makeText(
                                navController.context,
                                "Enter a positive whole number to add",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            currentQty = (currentQty ?: 0) + v
                            Toast.makeText(
                                navController.context,
                                "Added $v to stock (not yet persisted)",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) { Text("Add to Stock") }

                Button(
                    onClick = {
                        val v = delta.toIntOrNull()
                        if (v == null || v <= 0) {
                            Toast.makeText(
                                navController.context,
                                "Enter a positive whole number to remove",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            val next = (currentQty ?: 0) - v
                            currentQty = if (next < 0) 0 else next
                            Toast.makeText(
                                navController.context,
                                "Removed $v from stock (not yet persisted)",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) { Text("Remove from Stock") }
            }

            Spacer(Modifier.height(16.dp))
            Text("Where Used", style = MaterialTheme.typography.titleMedium)
            Text(
                "Products/assemblies that use this part will appear here.",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.height(24.dp)) // bottom breathing room
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("$label:", style = MaterialTheme.typography.bodyMedium)
        Text(value, style = MaterialTheme.typography.bodyLarge)
    }
}
