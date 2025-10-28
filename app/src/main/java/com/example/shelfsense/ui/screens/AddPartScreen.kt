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
import com.example.shelfsense.data.dao.StockItemDao
import com.example.shelfsense.data.entities.StockItem
import com.example.shelfsense.ui.components.AppScaffold
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun AddPartScreen(navController: NavController) {
    val context = LocalContext.current
    val db = remember { AppDatabase.get(context) }
    val dao: StockItemDao = remember { db.stockItemDao() }
    val scope = rememberCoroutineScope()

    var name by rememberSaveable { mutableStateOf("") }
    var code by rememberSaveable { mutableStateOf("") }
    var qty by rememberSaveable { mutableStateOf("") }
    var bin by rememberSaveable { mutableStateOf("") }

    AppScaffold(
        title = "Add Part"
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
                label = { Text("Part name") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )
            OutlinedTextField(
                value = code,
                onValueChange = { code = it },
                label = { Text("Part code / SKU") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Ascii,
                    imeAction = ImeAction.Next
                )
            )
            OutlinedTextField(
                value = qty,
                onValueChange = { qty = it },
                label = { Text("Quantity") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )
            )
            OutlinedTextField(
                value = bin,
                onValueChange = { bin = it },
                label = { Text("Bin / Location") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        scope.launch {
                            savePart(dao, name, code, qty, bin) { msg ->
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
                        savePart(dao, name, code, qty, bin) { msg ->
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

private suspend fun savePart(
    dao: StockItemDao,
    name: String,
    code: String,
    qty: String,
    bin: String,
    notify: (String) -> Unit
) {
    if (name.isBlank() || code.isBlank() || qty.isBlank()) {
        notify("Please fill all required fields")
        return
    }
    val q = qty.toIntOrNull()
    if (q == null) {
        notify("Quantity must be a whole number")
        return
    }
    withContext(Dispatchers.IO) {
        dao.insert(
            StockItem(
                name = name.trim(),
                sku = code.trim(),
                qty = q,
                bin = bin.trim()
            )
        )
    }
    notify("Saved part: $name")
}
