package com.example.shelfsense.ui.components

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ShelfSenseAppBar(title: String) {
    CenterAlignedTopAppBar(title = { Text(title) })
}
