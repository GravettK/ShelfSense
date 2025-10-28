package com.example.shelfsense.navigation

import androidx.navigation.NavController
import androidx.navigation.navOptions


fun NavController.navigateSingleTop(
    route: String,
    popUpToRoute: String? = null,
    inclusive: Boolean = false
) {
    val opts = navOptions {
        launchSingleTop = true
        popUpToRoute?.let { target ->
            popUpTo(target) { this.inclusive = inclusive }
        }
    }
    this.navigate(route, opts)
}
