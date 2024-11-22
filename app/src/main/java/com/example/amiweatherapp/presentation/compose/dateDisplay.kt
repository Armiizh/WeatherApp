package com.example.amiweatherapp.presentation.compose

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun dateDisplay(date: String): String {
    val currentDate = LocalDate.now()
    val inputDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"))

    val dayOfWeek = when (inputDate) {
        currentDate -> "Сегодня"
        else -> {
            val dayOfWeek = inputDate.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
            when (dayOfWeek) {
                "Mon" -> "Пн"
                "Tue" -> "Вт"
                "Wed" -> "Ср"
                "Thu" -> "Чт"
                "Fri" -> "Пт"
                "Sat" -> "Сб"
                "Sun" -> "Вс"
                else -> ""
            }
        }
    }
    return dayOfWeek
}