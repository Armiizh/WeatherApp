package com.example.amiweatherapp.presentation.compose

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size
import com.example.amiweatherapp.data.local.model.WeatherResponse
import com.example.amiweatherapp.data.local.model.Hour
import com.example.amiweatherapp.data.utils.Result
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HourlyForecast(result: Result.Success<WeatherResponse>, ctx: Context) {

    val currentDateTime = LocalDateTime.now()
    val currentHour = currentDateTime.format(DateTimeFormatter.ofPattern("HH"))
    val currentDate = LocalDate.now()
    val todayForecast = result.data.forecast.forecastday.find { it.date == currentDate.format(
        DateTimeFormatter.ofPattern("yyyy-MM-dd")) }
    val tomorrowForecast = result.data.forecast.forecastday.find { it.date == currentDate.plusDays(1).format(
        DateTimeFormatter.ofPattern("yyyy-MM-dd")) }

    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color.LightGray.copy(alpha = .2f))
    ) {
        Text(
            modifier = Modifier.padding(start = 16.dp, top = 8.dp),
            text = result.data.current.condition.text,
            color = Color.DarkGray,
            fontSize = 12.sp,
        )
        HorizontalDivider(
            Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = Color.LightGray.copy(alpha = .3f)
        )
        Row(
            Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
                .horizontalScroll(rememberScrollState())
        ) {
            val hourlyForecasts = mutableListOf<Hour>()
            todayForecast?.hour?.let { todayHours ->
                val filteredTodayHours = todayHours.filter { hour ->
                    hour.time.split(" ")[1].substring(0, 2) >= currentHour
                }
                hourlyForecasts.addAll(filteredTodayHours)
            }
            tomorrowForecast?.hour?.let { tomorrowHours ->
                hourlyForecasts.addAll(tomorrowHours)
            }

            if (hourlyForecasts.isNotEmpty()) {
                for (hour in hourlyForecasts) {
                    HourlyForecastItem(hour, ctx)
                }
            } else {
                Text("Нет доступных данных для оставшегося времени")
            }
        }
    }
}

@Composable
fun HourlyForecastItem(hour: Hour, ctx: Context) {
    Column(

        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val hourPart = hour.time.substring(11, 13)
        Text(hourPart)
        AsyncImage(
            modifier = Modifier
                .size(48.dp),
            model = ImageRequest.Builder(ctx)
                .data("https:${hour.condition.icon}")
                .size(Size.ORIGINAL)
                .build(),
            contentDescription = null
        )
        Text("${hour.temp_c.roundToInt()}°")
    }
}