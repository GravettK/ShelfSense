package com.example.shelfsense.ui.theme

import androidx.compose.ui.graphics.Color

// === MTI Brand Palette ===
val MTI_Blue = Color(0xFF1A237E)
val MTI_Red = Color(0xFFE53935)
val MTI_White = Color(0xFFFFFFFF)
val MTI_Charcoal = Color(0xFF212121)

// === Light Theme ===
val md_theme_light_primary = MTI_Blue
val md_theme_light_onPrimary = MTI_White
val md_theme_light_secondary = MTI_Red
val md_theme_light_onSecondary = MTI_White
val md_theme_light_background = MTI_White
val md_theme_light_onBackground = MTI_Charcoal
val md_theme_light_surface = MTI_White
val md_theme_light_onSurface = MTI_Charcoal

// === Dark Theme ===
val md_theme_dark_primary = MTI_Blue
val md_theme_dark_onPrimary = MTI_White
val md_theme_dark_secondary = MTI_Red
val md_theme_dark_onSecondary = MTI_White
val md_theme_dark_background = Color(0xFF121212)
val md_theme_dark_onBackground = MTI_White
val md_theme_dark_surface = Color(0xFF121212)
val md_theme_dark_onSurface = MTI_White

// === Semantic / Status Colors ===
val StockOk = Color(0xFF2E7D32)     // green for sufficient stock
val StockLow = MTI_Red              // red accent from brand
val StockWarning = Color(0xFFF9A825) // amber/yellow for low warning
