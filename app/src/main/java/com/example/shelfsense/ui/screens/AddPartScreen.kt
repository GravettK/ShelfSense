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
    var isSaving by rememberSaveable { mutableStateOf(false) }

    fun doSave() {
        if (isSaving) return
        scope.launch {
            isSaving = true
            val msg = savePart(dao, name, code, qty, bin)
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            isSaving = false
            if (msg.startsWith("Saved")) navController.popBackStack()
        }
    }

    AppScaffold(title = "Add Part") { innerPadding ->
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
                label = { Text("Part name") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                singleLine = true
            )

            OutlinedTextField(
                value = code,
                onValueChange = { code = it },
                label = { Text("Part code / SKU") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Ascii,
                    imeAction = ImeAction.Next
                ),
                singleLine = true
            )

            OutlinedTextField(
                value = qty,
                onValueChange = { qty = it.filter { ch -> ch.isDigit() } },
                label = { Text("Quantity") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                singleLine = true
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
                keyboardActions = KeyboardActions(onDone = { doSave() }),
                singleLine = true
            )

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = { doSave() },
                enabled = !isSaving && name.isNotBlank() && code.isNotBlank() && qty.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    if (isSaving) "Saving…" else "Save",
                    style = MaterialTheme.typography.labelLarge
                )
            }

            // Add a little bottom padding so the button isn't cramped above the nav bar
            Spacer(Modifier.height(24.dp))
        }
    }
}

/** Inserts/updates a StockItem. Returns a user-facing message. */
private suspend fun savePart(
    dao: StockItemDao,
    name: String,
    code: String,
    qty: String,
    bin: String
): String {
    val trimmedName = name.trim()
    val trimmedCode = code.trim()
    val trimmedBin = bin.trim()

    if (trimmedName.isEmpty() || trimmedCode.isEmpty() || qty.isBlank()) {
        return "Please fill all required fields"
    }

    val q = qty.toIntOrNull()
    if (q == null || q < 0) {
        return "Quantity must be a non-negative whole number"
    }

    return try {
        withContext(Dispatchers.IO) {
            dao.insert(
                StockItem(
                    name = trimmedName,
                    sku = trimmedCode,
                    qty = q,
                    bin = trimmedBin
                )
            )
        }
        "Saved part: $trimmedName"
    } catch (t: Throwable) {
        "Failed to save: ${t.message ?: "unknown error"}"
    }
}
