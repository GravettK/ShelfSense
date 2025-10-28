package com.example.shelfsense.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.shelfsense.data.AppDatabase
import com.example.shelfsense.data.dao.ProductDao
import com.example.shelfsense.data.entities.Product
import com.example.shelfsense.ui.components.AppScaffold
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun AddProductScreen(navController: NavController) {
    val context = LocalContext.current
    val db = remember { AppDatabase.get(context) }
    val dao: ProductDao = remember { db.productDao() }
    val scope = rememberCoroutineScope()

    var name by rememberSaveable { mutableStateOf("") }
    var sku by rememberSaveable { mutableStateOf("") }
    var cost by rememberSaveable { mutableStateOf("") }

    AppScaffold(
        title = "Add Product"
    ) { inner ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Product name") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )
            OutlinedTextField(
                value = sku,
                onValueChange = { sku = it },
                label = { Text("SKU") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Ascii,
                    imeAction = ImeAction.Next
                )
            )
            OutlinedTextField(
                value = cost,
                onValueChange = { cost = it },
                label = { Text("Cost") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        scope.launch {
                            saveProduct(dao, name, sku, cost) { msg ->
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                if (msg.startsWith("Saved")) navController.popBackStack()
                            }
                        }
                    }
                )
            )

            Spacer(Modifier.height(8.dp))
            Button(
                onClick = {
                    scope.launch {
                        saveProduct(dao, name, sku, cost) { msg ->
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                            if (msg.startsWith("Saved")) navController.popBackStack()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

private suspend fun saveProduct(
    dao: ProductDao,
    name: String,
    sku: String,
    cost: String,
    notify: (String) -> Unit
) {
    if (name.isBlank() || sku.isBlank() || cost.isBlank()) {
        notify("Please fill all fields")
        return
    }
    val price = cost.toDoubleOrNull()
    if (price == null) {
        notify("Cost must be a number")
        return
    }
    withContext(Dispatchers.IO) {
        dao.insert(
            Product(
                name = name.trim(),
                sku = sku.trim(),
                cost = price
            )
        )
    }
    notify("Saved product: $name")
}
