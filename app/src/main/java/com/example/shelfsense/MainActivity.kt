package com.example.shelfsense

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.example.shelfsense.navigation.AppNavigation
import com.example.shelfsense.ui.theme.ShelfSenseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            ShelfSenseTheme {
                AppNavigation()
            }
        }
        setContent {
            ShelfSenseTheme(dynamicColor = false) {
                AppNavigation()
            }
        }
    }
}
