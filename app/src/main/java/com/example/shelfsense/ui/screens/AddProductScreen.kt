package com.example.shelfsense.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.foundation.text.KeyboardOptions   // ✅ correct package
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.shelfsense.data.repository.MockData
import com.example.shelfsense.data.repository.Part
import com.example.shelfsense.data.repository.ProductModel
import com.example.shelfsense.ui.components.CenteredScreenTitle
import com.example.shelfsense.ui.theme.Dimens

@Composable
fun AddProductScreen(navController: NavController) {
    var code by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var width by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var length by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }

    val stock = MockData.stock
    val qtyBySku = remember { mutableStateMapOf<String, String>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CenteredScreenTitle("Add Product")

        OutlinedTextField(value = code, onValueChange = { code = it }, label = { Text("Product code") }, singleLine = true, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") }, singleLine = true, modifier = Modifier.fillMaxWidth())

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = width, onValueChange = { width = it }, label = { Text("Width (mm)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true, modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = height, onValueChange = { height = it }, label = { Text("Height (mm)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true, modifier = Modifier.weight(1f)
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = length, onValueChange = { length = it }, label = { Text("Length (mm)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true, modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = weight, onValueChange = { weight = it }, label = { Text("Weight (kg)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true, modifier = Modifier.weight(1f)
            )
        }

        Text("Parts per unit (optional)", style = MaterialTheme.typography.titleMedium)

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.weight(1f)) {
            items(stock) { item ->
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    Text(item.name, modifier = Modifier.weight(1f))
                    OutlinedTextField(
                        value = qtyBySku[item.sku] ?: "",
                        onValueChange = { qtyBySku[item.sku] = it },
                        label = { Text("Qty") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.width(90.dp)
                    )
                }
            }
        }

        Button(
            onClick = {
                val parts = qtyBySku.mapNotNull { (sku, qtyTxt) ->
                    val qty = qtyTxt.toIntOrNull() ?: 0
                    if (qty > 0) {
                        val nameFromStock = MockData.getStock(sku)?.name ?: sku
                        Part(sku = sku, name = nameFromStock, qtyPerUnit = qty)
                    } else null
                }

                val model = ProductModel(
                    code = code.trim(),
                    name = name.trim(),
                    widthMm = width.toIntOrNull() ?: 0,
                    heightMm = height.toIntOrNull() ?: 0,
                    lengthMm = length.toIntOrNull() ?: 0,
                    weightKg = weight.toIntOrNull() ?: 0,
                    parts = parts
                )
                MockData.addProduct(model)
                navController.popBackStack()
            },
            enabled = code.isNotBlank() && name.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Product")
        }
    }
}
