
package com.example.shelfsense.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BottomSheetAdjuster(onPlus: () -> Unit, onMinus: () -> Unit, onCustom: (Int) -> Unit) {
    Column(Modifier.padding(16.dp)) {
        Text("Adjust quantity")
        Button(onClick = onPlus, modifier = Modifier.padding(top = 8.dp)) { Text("+1") }
        Button(onClick = onMinus, modifier = Modifier.padding(top = 8.dp)) { Text("-1") }
        Button(onClick = { onCustom(0) }, modifier = Modifier.padding(top = 8.dp)) { Text("Custom…") }
    }
}
