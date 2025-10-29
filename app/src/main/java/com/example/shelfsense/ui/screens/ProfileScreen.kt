package com.example.shelfsense.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.shelfsense.navigation.Routes
import com.example.shelfsense.repository.AuthRepository
import com.example.shelfsense.ui.components.AppScaffold
import com.example.shelfsense.ui.components.BottomNavBar

@Composable
fun ProfileScreen(navController: NavController) {
    val context = navController.context
    val email = AuthRepository.getUserEmail(context) ?: "Unknown User"

    AppScaffold(
        title = "Profile",
        bottomBar = { BottomNavBar(navController) }
    ) { innerPadding ->
        val scroll = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(scroll),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Profile Information",
                style = MaterialTheme.typography.titleLarge
            )

            Divider()

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Logged in as:",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = email,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(Modifier.height(24.dp))

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

            Spacer(Modifier.height(24.dp)) // bottom breathing room
        }
    }
}
