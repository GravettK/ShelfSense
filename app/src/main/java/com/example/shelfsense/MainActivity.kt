package com.example.shelfsense

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.shelfsense.navigation.AppNavigation
import com.example.shelfsense.navigation.Routes
import com.example.shelfsense.ui.theme.ShelfSenseTheme
import androidx.compose.runtime.Composable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { ShelfSenseTheme { AppRoot() } }
    }
}

@Composable
private fun AppRoot() {
    val nav = rememberNavController()
    AppNavigation(navController = nav, start = Routes.HOME)
}
