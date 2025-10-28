package com.example.shelfsense.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class BadgeStyle { INFO, SUCCESS, WARNING, ERROR }

/**
 * Base badge (your component).
 */
@Composable
fun StatusBadge(
    label: String,
    style: BadgeStyle,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor) = when (style) {
        BadgeStyle.INFO -> Pair(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.onPrimary)
        BadgeStyle.SUCCESS -> Pair(MaterialTheme.colorScheme.tertiary, MaterialTheme.colorScheme.onTertiary)
        BadgeStyle.WARNING -> Pair(MaterialTheme.colorScheme.secondary, MaterialTheme.colorScheme.onSecondary)
        BadgeStyle.ERROR -> Pair(MaterialTheme.colorScheme.error, MaterialTheme.colorScheme.onError)
    }

    Box(
        modifier = modifier
            .background(bgColor, RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = label.uppercase(),
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

/**
 * Convenience mapper: order status -> BadgeStyle.
 */
fun badgeStyleForStatus(status: String): BadgeStyle =
    when (status.trim().lowercase()) {
        "completed" -> BadgeStyle.SUCCESS
        "in progress" -> BadgeStyle.INFO
        "on hold" -> BadgeStyle.WARNING
        "cancelled" -> BadgeStyle.ERROR
        "pending" -> BadgeStyle.WARNING
        else -> BadgeStyle.INFO
    }
