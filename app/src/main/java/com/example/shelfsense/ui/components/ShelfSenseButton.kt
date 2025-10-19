package com.example.shelfsense.ui.components

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ShelfSenseButton(text: String, onClick: () -> Unit) {
    Button(onClick = onClick) { Text(text) }
}
