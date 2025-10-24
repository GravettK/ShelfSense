package com.example.shelfsense.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.shelfsense.R
import com.example.shelfsense.data.repository.MockData
import com.example.shelfsense.ui.components.CenteredScreenTitle
import com.example.shelfsense.ui.theme.Dimens

@Composable
fun ProductDetailScreen(navController: NavController, modelCode: String?) {
    val product = modelCode?.let { MockData.getProduct(it) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CenteredScreenTitle(product?.name ?: "Product Detail")

        if (product == null) {
            Text("Product not found.", style = MaterialTheme.typography.bodyMedium)
            return@Column
        }

        // Photo (placeholder tank icon)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Product image",
                    modifier = Modifier.size(64.dp),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                )
                Column {
                    Text("Code: ${product.code}", style = MaterialTheme.typography.bodyMedium)
                    Text("Size: ${product.lengthMm}×${product.widthMm}×${product.heightMm} mm",
                        style = MaterialTheme.typography.bodyMedium)
                    Text("Weight: ${product.weightKg} kg", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        Text("Parts for 1 unit", style = MaterialTheme.typography.titleMedium)
        Divider()

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxSize()) {
            items(product.parts) { part ->
                val stock = MockData.getStock(part.sku)?.onHand ?: 0
                val ok = stock >= part.qtyPerUnit
                PartRow(
                    name = "${part.name}  (x${part.qtyPerUnit})",
                    sku = part.sku,
                    ok = ok,
                    note = if (!ok) "short ${part.qtyPerUnit - stock}" else null
                )
            }
        }
    }
}

@Composable
private fun PartRow(name: String, sku: String, ok: Boolean, note: String?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(name, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium))
            Text("SKU: $sku", style = MaterialTheme.typography.labelMedium)
        }
        Text(
            text = if (ok) "✅" else "❌",
            style = MaterialTheme.typography.titleLarge
        )
        if (note != null) {
            Spacer(Modifier.width(8.dp))
            Text(note, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.error)
        }
    }
}
