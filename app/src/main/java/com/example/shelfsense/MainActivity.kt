package com.example.shelfsense

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import com.example.shelfsense.navigation.AppNavigation
import com.example.shelfsense.navigation.Routes
import com.example.shelfsense.ui.theme.ShelfSenseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShelfSenseTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    AppNavigation(
                        navController = navController,
                        start = Routes.HOME                               // <- land on Home when logged in
                    )
                }
            }
        }
    }
}
