package com.example.shelfsense.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add

/**
 * App shell with colored top bar, optional FAB, and optional bottom bar.
 * Uses a plain FAB for broad Material3 compatibility.
 */
@Composable
fun AppScaffold(
    title: String,
    modifier: Modifier = Modifier,
    onFabClick: (() -> Unit)? = null,
    fabLabel: String = "Add",
    fab: (@Composable () -> Unit)? = null,
    bottomBar: (@Composable () -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    val topBarColors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.primary,
        titleContentColor = MaterialTheme.colorScheme.onPrimary,
        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
        actionIconContentColor = MaterialTheme.colorScheme.onPrimary
    )

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(title) },
                colors = topBarColors
            )
        },

        floatingActionButton = {
            when {
                fab != null -> fab()
                onFabClick != null -> {
                    FloatingActionButton(onClick = onFabClick) {
                        // simple icon; if you really want text, wrap Row { Icon(); Spacer(); Text(fabLabel) }
                        Icon(Icons.Filled.Add, contentDescription = fabLabel)
                    }
                }
            }
        },
        bottomBar = { bottomBar?.invoke() },
        content = content
    )
}
