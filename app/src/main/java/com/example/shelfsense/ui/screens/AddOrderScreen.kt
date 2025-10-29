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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.shelfsense.navigation.Routes
import com.example.shelfsense.ui.components.AppScaffold
import com.example.shelfsense.ui.components.BottomNavBar
import com.example.shelfsense.ui.components.ScreenColumn

private data class OrderLineDraft(
    val type: LineType,
    val sku: String,
    var qty: Int = 1
)

private enum class LineType { PRODUCT, PART }

@Composable
fun AddOrderScreen(navController: NavController) {
    var orderNo by rememberSaveable { mutableStateOf("") }
    var customer by rememberSaveable { mutableStateOf("") }
    var error by rememberSaveable { mutableStateOf<String?>(null) }

    val lines = remember { mutableStateListOf<OrderLineDraft>() }

    val savedState = navController.currentBackStackEntry?.savedStateHandle
    LaunchedEffect(savedState) {
        savedState?.getStateFlow<String?>("selected_product_sku", null)?.collect { sku ->
            if (!sku.isNullOrBlank()) {
                lines.add(OrderLineDraft(LineType.PRODUCT, sku))
                navController.currentBackStackEntry?.savedStateHandle?.set("selected_product_sku", null)
            }
        }
    }
    LaunchedEffect(savedState) {
        savedState?.getStateFlow<String?>("selected_part_sku", null)?.collect { sku ->
            if (!sku.isNullOrBlank()) {
                lines.add(OrderLineDraft(LineType.PART, sku))
                navController.currentBackStackEntry?.savedStateHandle?.set("selected_part_sku", null)
            }
        }
    }

    AppScaffold(
        title = "Add Order",
        bottomBar = { BottomNavBar(navController) }
    ) { pv ->
        ScreenColumn(padding = pv) {
            OutlinedTextField(
                value = orderNo,
                onValueChange = { orderNo = it },
                label = { Text("Order number") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = customer,
                onValueChange = { customer = it },
                label = { Text("Customer") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            if (error != null) {
                Text(
                    text = error!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(Modifier.height(8.dp))
            Divider()
            Text("Lines", style = MaterialTheme.typography.titleMedium)

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                TextButton(onClick = { navController.navigate("select_product") }) {
                    Text("Add Product from Catalog")
                }
                TextButton(onClick = { navController.navigate("select_part") }) {
                    Text("Add Part from Stock")
                }
            }

            if (lines.isEmpty()) {
                Text(
                    "No lines yet. Use the buttons above to add products/parts.",
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                lines.forEachIndexed { index, line ->
                    LineEditor(
                        line = line,
                        onQtyChange = { q -> if (q >= 0) lines[index] = line.copy(qty = q) },
                        onRemove = { lines.removeAt(index) }
                    )
                    Divider()
                }
            }

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = {
                    if (orderNo.isBlank() || customer.isBlank()) {
                        error = "Please fill in Order number and Customer."
                        return@Button
                    }
                    if (lines.isEmpty()) {
                        error = "Please add at least one line."
                        return@Button
                    }
                    error = null
                    Toast.makeText(
                        navController.context,
                        "Saved order $orderNo with ${lines.size} line(s) (demo).",
                        Toast.LENGTH_SHORT
                    ).show()
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Order")
            }
        }
    }
}

@Composable
private fun LineEditor(
    line: OrderLineDraft,
    onQtyChange: (Int) -> Unit,
    onRemove: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val label = if (line.type == LineType.PRODUCT) "Product" else "Part"
            Text("$label • SKU: ${line.sku}", style = MaterialTheme.typography.titleSmall)
            IconButton(onClick = onRemove) {
                Icon(Icons.Filled.Delete, contentDescription = "Remove line")
            }
        }
        OutlinedTextField(
            value = line.qty.toString(),
            onValueChange = { s -> onQtyChange(s.toIntOrNull() ?: 0) },
            label = { Text("Quantity") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
