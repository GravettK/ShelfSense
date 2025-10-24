package com.example.shelfsense.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.shelfsense.data.repository.MockData
import com.example.shelfsense.navigation.Routes
import com.example.shelfsense.ui.components.CenteredScreenTitle
import com.example.shelfsense.ui.theme.Dimens

@Composable
fun OrderDetailScreen(navController: NavController, orderNo: String?) {
    val order = orderNo?.let { MockData.getOrder(it) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CenteredScreenTitle("Order ${order?.orderNo ?: ""}")

        if (order == null) {
            Text("Order not found.", style = MaterialTheme.typography.bodyMedium)
            return@Column
        }

        // Summary header
        Text("Customer: ${order.customer}", style = MaterialTheme.typography.bodyMedium)
        Text("Due: ${order.dueDate}   Priority: ${order.priority}", style = MaterialTheme.typography.bodyMedium)

        Spacer(Modifier.height(8.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxSize()) {
            items(order.lines) { line ->
                val product = MockData.getProduct(line.modelCode)
                val missingSummary = product?.parts
                    ?.mapNotNull { part ->
                        val stock = MockData.getStock(part.sku)?.onHand ?: 0
                        val needed = part.qtyPerUnit * line.qty
                        if (stock < needed) "${part.name} (${needed - stock} short)" else null
                    }
                    ?.joinToString(separator = ", ")
                    ?.takeIf { it.isNotEmpty() }

                OrderLineCard(
                    title = "${product?.name ?: line.modelCode}",
                    subtitle = "Qty: ${line.qty}",
                    availability = missingSummary ?: "All parts available",
                    isOk = missingSummary == null,
                    onClick = {
                        // Go to product detail for this model (one unit view)
                        navController.navigate(Routes.productDetail(line.modelCode))
                    }
                )
            }
        }
    }
}

@Composable
private fun OrderLineCard(
    title: String,
    subtitle: String,
    availability: String,
    isOk: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(Modifier.padding(Dimens.CardPadding)) {
            Text(title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold))
            Spacer(Modifier.height(2.dp))
            Text(subtitle, style = MaterialTheme.typography.labelMedium)
            Spacer(Modifier.height(6.dp))
            Text(
                text = availability,
                style = MaterialTheme.typography.labelSmall,
                color = if (isOk) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            )
        }
    }
}
