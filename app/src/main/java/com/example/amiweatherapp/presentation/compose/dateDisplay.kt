package com.example.amiweatherapp.presentation.compose

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun dateDisplay(date: String): String {
    val currentDate = LocalDate.now()
    val inputDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    return when {
        inputDate == currentDate -> "Сегодня"
        else -> {
            val dayOfWeekValue = inputDate.dayOfWeek.value
            val daysInRussian = arrayOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс")
            daysInRussian[dayOfWeekValue - 1]
        }
    }
}