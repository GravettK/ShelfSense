package com.example.shelfsense.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

enum class BadgeStyle { INFO, SUCCESS, WARNING, ERROR, NEUTRAL }

@Composable
fun StatusBadge(
    label: String,
    style: BadgeStyle,
    modifier: Modifier = Modifier
) {
    val (bg, fg) = when (style) {
        BadgeStyle.INFO    -> MaterialTheme.colorScheme.primary to MaterialTheme.colorScheme.onPrimary
        BadgeStyle.SUCCESS -> Color(0xFF2E7D32) to Color.White
        BadgeStyle.WARNING -> Color(0xFFFFA000) to Color.Black
        BadgeStyle.ERROR   -> Color(0xFFD32F2F) to Color.White
        BadgeStyle.NEUTRAL -> MaterialTheme.colorScheme.surfaceVariant to MaterialTheme.colorScheme.onSurfaceVariant
    }

    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .background(bg)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = label.uppercase(),
            color = fg,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
        )
    }
}
