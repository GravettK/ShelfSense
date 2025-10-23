package com.example.shelfsense.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AppScaffold(
    title: String,
    // Center FAB
    onFabClick: (() -> Unit)?,
    fabLabel: String = "Scan",
    // Top-bar actions (optional)
    topBarActions: (@Composable () -> Unit)? = null,
    // Bottom bar content (pass BottomNavBar or nothing)
    bottomBar: (@Composable () -> Unit)? = null,
    content: @Composable (Modifier) -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Scaffold(
        topBar = {
            // Center-aligned top app bar for consistent centered title
            CenterAlignedTopAppBar(
                title = { Text(title) },
                actions = { topBarActions?.invoke() },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = colors.primary,
                    titleContentColor = colors.onPrimary,
                    actionIconContentColor = colors.onPrimary,
                    navigationIconContentColor = colors.onPrimary
                )
            )
        },
        floatingActionButton = {
            if (onFabClick != null) {
                ExtendedFloatingActionButton(
                    onClick = onFabClick,
                    icon = { Icon(Icons.Filled.QrCodeScanner, contentDescription = null) },
                    text = { Text(fabLabel) },
                    containerColor = colors.primary,
                    contentColor = colors.onPrimary,
                    modifier = Modifier.padding(8.dp)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center, // ⬅ centered big Scan
        bottomBar = { bottomBar?.invoke() },
        containerColor = colors.background
    ) { innerPadding ->
        Surface(color = colors.background, contentColor = colors.onBackground) {
            content(Modifier.padding(innerPadding))
        }
    }
}
