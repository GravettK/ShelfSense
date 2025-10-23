package com.example.shelfsense.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BottomSheetAdjuster(onPlus: () -> Unit, onMinus: () -> Unit, onCustom: (Int) -> Unit) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Adjust quantity",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Button(onClick = onPlus, modifier = Modifier.padding(top = 8.dp)) { Text("+1") }
        Button(onClick = onMinus, modifier = Modifier.padding(top = 8.dp)) { Text("-1") }
        Button(onClick = { onCustom(0) }, modifier = Modifier.padding(top = 8.dp)) { Text("Custom…") }
    }
}
