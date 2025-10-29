package com.example.shelfsense.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.Layout
import androidx.compose.foundation.layout.padding

@Composable
fun ScreenColumn(
    padding: PaddingValues,
    modifier: Modifier = Modifier,
    verticalSpacing: Int = 12,
    content: @Composable ColumnScope.() -> Unit
) {
    val scroll = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(padding)     // Scaffold insets (top bar / bottom bar)
            .verticalScroll(scroll)
            .imePadding()         // lift above keyboard
            .padding(16.dp),      // page content padding
        verticalArrangement = Arrangement.spacedBy(verticalSpacing.dp)
    ) {
        content()
    }
}
