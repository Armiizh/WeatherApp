package com.example.amiweatherapp.presentation.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TemperatureGradientBox(
    currentTemperature: Double,
    minTemperature: Double,
    maxTemperature: Double
) {
    val normalizedTemperature = when {
        currentTemperature < minTemperature -> 0.0
        currentTemperature > maxTemperature -> 1.0
        else -> (currentTemperature - minTemperature) / (maxTemperature - minTemperature)
    }

    val colors = when {
        normalizedTemperature < 0.25 -> listOf(
            Color(0xFF00008B),
            Color(0xFF00BFFF)
        )
        normalizedTemperature in 0.25..0.5 -> listOf(
            Color(0xFF00BFFF),
            Color(0xFFFFA500)
        )
        normalizedTemperature in 0.5..0.75 -> listOf(
            Color(0xFFFFA500),
            Color(0xFFFF4500)
        )
        else -> listOf(Color(0xFFFF4500), Color(0xFFFFD700))
    }

    Box(
        modifier = Modifier
            .width(50.dp)
            .height(1.dp)
            .background(
                brush = Brush.horizontalGradient(colors)
            )
    )
}