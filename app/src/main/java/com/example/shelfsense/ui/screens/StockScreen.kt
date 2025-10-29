package com.example.shelfsense.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.shelfsense.data.AppDatabase
import com.example.shelfsense.data.entities.StockItem
import com.example.shelfsense.navigation.Routes
import com.example.shelfsense.ui.components.AppScaffold
import com.example.shelfsense.ui.components.BottomNavBar
import com.example.shelfsense.ui.components.ScreenColumn
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

data class UiPart(
    val name: String,
    val sku: String,
    val qty: Int,
    val bin: String
)

@Composable
fun StockScreen(navController: NavController) {
    val context = LocalContext.current
    val db = remember { AppDatabase.get(context) }
    val dao = remember { db.stockItemDao() }
    val scope = rememberCoroutineScope()

    var query by rememberSaveable { mutableStateOf("") }
    var parts by remember { mutableStateOf<List<UiPart>>(emptyList()) }

    // Stream parts from Room (Flow). Use catch to prevent crashes on schema issues.
    LaunchedEffect(dao) {
        scope.launch {
            dao.observeAll() // Flow<List<StockItem>>
                .map { list -> list.map { it.toUiPart() } }
                .catch { parts = emptyList() }
                .collectLatest { parts = it }
        }
    }

    val filtered = remember(query, parts) {
        if (query.isBlank()) parts
        else parts.filter {
            it.name.contains(query, ignoreCase = true) ||
                    it.sku.contains(query, ignoreCase = true)
        }
    }

    AppScaffold(
        title = "Stock",
        bottomBar = { BottomNavBar(navController) },
        onFabClick = { navController.navigate(Routes.ADD_PART) },
        fabLabel = "Add Part"
    ) { pv ->
        ScreenColumn(padding = pv) {

            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Search stock") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            if (filtered.isEmpty()) {
                Text(
                    text = if (query.isBlank())
                        "No parts yet. Tap “Add Part” to create one."
                    else
                        "No parts match “$query”.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filtered, key = { it.sku }) { p ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    navController.navigate("${Routes.PART_DETAIL_ROOT}/${p.sku}")
                                }
                                .padding(vertical = 8.dp)
                        ) {
                            Text(p.name, style = MaterialTheme.typography.titleMedium)
                            Text(
                                text = "Code: ${p.sku}  •  Qty: ${p.qty}  •  Bin: ${p.bin}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        Divider()
                    }
                }
            }
        }
    }
}

private fun StockItem.toUiPart(): UiPart =
    UiPart(
        name = name,
        sku = sku,
        qty = qty,
        bin = bin
    )
