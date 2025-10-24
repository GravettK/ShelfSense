package com.example.shelfsense.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.foundation.text.KeyboardOptions   // ✅ correct package
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.shelfsense.data.entities.StockItem
import com.example.shelfsense.data.repository.MockData
import com.example.shelfsense.ui.components.CenteredScreenTitle
import com.example.shelfsense.ui.theme.Dimens

@Composable
fun AddPartScreen(navController: NavController) {
    var sku by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var onHand by remember { mutableStateOf("") }
    var minLevel by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CenteredScreenTitle("Add Part")

        OutlinedTextField(
            value = sku, onValueChange = { sku = it },
            label = { Text("SKU") }, singleLine = true, modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = name, onValueChange = { name = it },
            label = { Text("Name") }, singleLine = true, modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = onHand, onValueChange = { onHand = it },
            label = { Text("On hand") }, singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = minLevel, onValueChange = { minLevel = it },
            label = { Text("Minimum level") }, singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = {
                val item = StockItem(
                    sku = sku.trim(),
                    name = name.trim(),
                    onHand = onHand.toIntOrNull() ?: 0,
                    minLevel = minLevel.toIntOrNull() ?: 0
                )
                MockData.addStockItem(item)
                navController.popBackStack()
            },
            enabled = sku.isNotBlank() && name.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save", style = MaterialTheme.typography.labelLarge)
        }
    }
}
