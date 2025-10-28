package com.example.shelfsense.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.shelfsense.navigation.Routes
import com.example.shelfsense.repository.AuthRepository

@Composable
fun ProfileScreen(navController: NavController) {
    val context = navController.context
    val email = AuthRepository.getUserEmail(context) ?: "Unknown User"

    Scaffold(topBar = { TopAppBar(title = { Text("Profile") }) }) { pv ->
        Column(
            modifier = Modifier
                .padding(pv)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text("Logged in as:", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))
            Text(email, style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.height(20.dp))

            Button(
                onClick = {
                    AuthRepository.logout(context)
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Logout")
            }
        }
    }
}
