package com.example.amiweatherapp.presentation.compose

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.amiweatherapp.R
import com.example.amiweatherapp.data.utils.Result
import com.example.amiweatherapp.presentation.HomeViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Background(viewModel: HomeViewModel) {

    val weatherData by viewModel.weatherData.collectAsState()
    when (val result = weatherData) {
        is Result.Success -> {

            // Получаем текущий час
            val currentHour = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH"))
            val todayForecast = result.data.forecast.forecastday.firstOrNull {
                it.date == LocalDateTime.now().toLocalDate().toString()
            }
            val currentHourData = todayForecast?.hour?.find { hour ->
                hour.time.split(" ")[1].substring(0, 2) == currentHour
            }
            val current = result.data.current

            // Получаем is_day из текущего часа
            val isDay = currentHourData?.is_day ?: current.is_day

            var id by remember { mutableIntStateOf(0) }

            when (current.condition.code) {
                1000 -> {
                    id = if (isDay == 1) {
                        R.drawable.clear_day
                    } else {
                        R.drawable.clear_night
                    }
                }

                1003 -> {
                    id = if (isDay == 1) {
                        R.drawable.partly_cloudy_day
                    } else {
                        R.drawable.partly_cloudy_night
                    }
                }

                1006 -> {
                    id = if (isDay == 1) {
                        R.drawable.cloudy_day
                    } else {
                        R.drawable.cloudy_night
                    }
                }

                1009 -> {
                    id = if (isDay == 1) {
                        R.drawable.overcast_day
                    } else {
                        R.drawable.overcast_night
                    }
                }
            }

            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(id = id),
                contentDescription = null,
                contentScale = ContentScale.FillBounds
            )
        }

        is Result.Error -> {
            Gradient()
        }

        null -> {
            Gradient()
        }
    }
}

@Composable
fun Gradient() {
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF87CEEB),
            Color(0xFF4682B4)
        )
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush)
    )
}