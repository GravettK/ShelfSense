package com.example.shelfsense.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.shelfsense.navigation.Routes
import com.example.shelfsense.ui.components.AppScaffold
import com.example.shelfsense.ui.components.BottomNavBar

data class UiPart(
    val name: String,
    val sku: String,
    val qty: Int,
    val bin: String
)

@Composable
fun StockScreen(navController: NavController) {
    var q by remember { mutableStateOf("") }
    val parts = remember {
        mutableStateListOf(
            UiPart("12 Bret", "12b", 15, "2")
        )
    }
    val filtered = parts.filter { q.isBlank() || it.name.contains(q, true) || it.sku.contains(q, true) }

    AppScaffold(
        title = "Stock",
        bottomBar = { BottomNavBar(navController) }
    ) { pv: PaddingValues ->
        Column(Modifier.padding(pv).fillMaxSize()) {
            OutlinedTextField(
                value = q,
                onValueChange = { q = it },
                label = { Text("Search stock") },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
            )
            LazyColumn(Modifier.fillMaxSize()) {
                items(filtered, key = { it.sku }) { p ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable { navController.navigate(Routes.partDetail(p.sku)) }
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Text(p.name)
                        Text("Code: ${p.sku}  •  Qty: ${p.qty}  •  Bin: ${p.bin}")
                    }
                    Divider()
                }
            }
        }
    }
}
