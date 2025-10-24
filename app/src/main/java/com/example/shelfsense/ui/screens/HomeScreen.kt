package com.example.shelfsense.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.shelfsense.navigation.Routes
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// --- Orders model (swap to Room/Retrofit later) ---
enum class OrderStatus { PENDING, IN_PRODUCTION, READY, DELAYED }
enum class Priority { LOW, NORMAL, HIGH }

data class OrderItem(
    val orderNo: String,
    val model: String,          // Tank type/model
    val customer: String,
    val quantity: Int,
    val due: LocalDate,
    val status: OrderStatus,
    val priority: Priority = Priority.NORMAL
)

@Composable
fun HomeScreen(navController: NavController) {
    var query by remember { mutableStateOf("") }

    // Sample orders – replace with repository later
    val all = remember {
        listOf(
            OrderItem("SO-10231", "Cylindrical 500L", "Acme Water", 4, LocalDate.now().plusDays(3), OrderStatus.IN_PRODUCTION, Priority.HIGH),
            OrderItem("SO-10218", "Rectangular 300L", "BlueRiver Farms", 2, LocalDate.now().plusDays(1), OrderStatus.DELAYED, Priority.HIGH),
            OrderItem("SO-10210", "Vertical 1000L", "HydroBuild", 1, LocalDate.now().plusDays(7), OrderStatus.PENDING),
            OrderItem("SO-10196", "Horizontal 750L", "CityWorks", 3, LocalDate.now().plusDays(2), OrderStatus.READY),
        )
    }

    val filtered = all.filter {
        val q = query.trim()
        q.isEmpty() ||
                it.orderNo.contains(q, true) ||
                it.model.contains(q, true) ||
                it.customer.contains(q, true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Search orders…") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(filtered) { order ->
                OrderCardRow(
                    item = order,
                    onClick = { navController.navigate(Routes.orderDetail(order.orderNo)) }
                )
            }
            item { Spacer(Modifier.height(64.dp)) } // space above FAB
        }
    }
}

@Composable
private fun OrderCardRow(item: OrderItem, onClick: () -> Unit) {
    val df = remember { DateTimeFormatter.ofPattern("dd MMM") }
    val dueText = item.due.format(df)

    // ✅ compute theme-derived color in composable scope
    val colorScheme = MaterialTheme.colorScheme
    val chipColor = when (item.status) {
        OrderStatus.PENDING        -> colorScheme.secondary      // red accent from brand
        OrderStatus.IN_PRODUCTION  -> colorScheme.primary
        OrderStatus.READY          -> Color(0xFF2E7D32)          // green
        OrderStatus.DELAYED        -> colorScheme.error          // (define in Color.kt if you want a custom red)
    }
    val chipText = when (item.status) {
        OrderStatus.PENDING -> "PENDING"
        OrderStatus.IN_PRODUCTION -> "IN PRODUCTION"
        OrderStatus.READY -> "READY"
        OrderStatus.DELAYED -> "DELAYED"
    }

    Card(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(14.dp)) {

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = item.orderNo,
                    style = MaterialTheme.typography.titleMedium
                )
                StatusChip(text = chipText, color = chipColor)
            }

            Spacer(Modifier.height(4.dp))
            Text(text = item.model, style = MaterialTheme.typography.bodyMedium)

            Spacer(Modifier.height(2.dp))
            Text(
                text = "Customer: ${item.customer}",
                style = MaterialTheme.typography.labelMedium,
                color = colorScheme.onSurface.copy(alpha = 0.75f)
            )

            Spacer(Modifier.height(8.dp))
            Divider()
            Spacer(Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Meta("Qty: ${item.quantity}")
                Meta("Due: $dueText")
                Meta(priorityLabel(item.priority))
            }
        }
    }
}

@Composable
private fun StatusChip(text: String, color: Color) {
    Surface(
        color = color.copy(alpha = 0.12f),
        contentColor = color,
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun Meta(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
    )
}

// Keep only this non-composable helper (no theme access)
private fun priorityLabel(p: Priority) = when (p) {
    Priority.LOW -> "Priority: Low"
    Priority.NORMAL -> "Priority: Normal"
    Priority.HIGH -> "Priority: High"
}
