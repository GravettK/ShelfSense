
package com.example.shelfsense.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun LoginScreen(navController: NavController) {{
    Scaffold(
        topBar = {{ TopAppBar(title = {{ Text("LoginScreen") }}) }}
    ) {{ padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {{
            Text("This is the LoginScreen screen")
        }}
    }}
}}
}