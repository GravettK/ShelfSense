package com.example.shelfsense.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
    var isSaving by rememberSaveable { mutableStateOf(false) }

    fun doSave() {
        if (isSaving) return
        scope.launch {
            isSaving = true
            val msg = saveProduct(dao, name, sku, cost)
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            isSaving = false
            if (msg.startsWith("Saved")) navController.popBackStack()
        }
    }

    AppScaffold(title = "Add Product") { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
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
                ),
                singleLine = true
            )

            OutlinedTextField(
                value = sku,
                onValueChange = { sku = it },
                label = { Text("SKU") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Ascii,
                    imeAction = ImeAction.Next
                ),
                singleLine = true
            )

            OutlinedTextField(
                value = cost,
                onValueChange = { input ->
                    // Allow only digits and a single decimal point (commas normalized to dot).
                    val normalized = input.replace(',', '.')
                    var seenDot = false
                    val cleaned = buildString {
                        for (ch in normalized) {
                            when {
                                ch.isDigit() -> append(ch)
                                ch == '.' && !seenDot -> {
                                    append('.'); seenDot = true
                                }
                                else -> Unit
                            }
                        }
                    }
                    cost = cleaned
                },
                label = { Text("Cost") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { doSave() }),
                singleLine = true
            )

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = { doSave() },
                enabled = !isSaving && name.isNotBlank() && sku.isNotBlank() && cost.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    if (isSaving) "Saving…" else "Save",
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

private suspend fun saveProduct(
    dao: ProductDao,
    name: String,
    sku: String,
    cost: String
): String {
    val n = name.trim()
    val s = sku.trim()
    val cStr = cost.trim().replace(',', '.')

    if (n.isEmpty() || s.isEmpty() || cStr.isEmpty()) return "Please fill all fields"

    val price = cStr.toDoubleOrNull()
    if (price == null || price < 0.0) return "Cost must be a non-negative number"

    return try {
        withContext(Dispatchers.IO) {
            dao.insert(Product(name = n, sku = s, cost = price))
        }
        "Saved product: $n"
    } catch (t: Throwable) {
        "Failed to save: ${t.message ?: "unknown error"}"
    }
}
