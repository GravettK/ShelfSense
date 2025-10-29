package com.example.shelfsense.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BottomSheetAdjuster(
    onPlus: () -> Unit,
    onMinus: () -> Unit,
    onCustom: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Adjust Quantity",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Button(onClick = onPlus, modifier = Modifier.fillMaxWidth(0.8f)) {
            Text("+1")
        }
        Button(onClick = onMinus, modifier = Modifier.fillMaxWidth(0.8f)) {
            Text("-1")
        }
        Button(onClick = { onCustom(0) }, modifier = Modifier.fillMaxWidth(0.8f)) {
            Text("Custom…")
        }
    }
}
