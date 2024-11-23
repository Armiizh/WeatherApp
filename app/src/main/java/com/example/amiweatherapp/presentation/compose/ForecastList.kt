package com.example.amiweatherapp.presentation.compose

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size
import com.example.amiweatherapp.R
import com.example.amiweatherapp.data.local.model.ForecastDay
import com.example.amiweatherapp.data.local.model.WeatherResponse
import com.example.amiweatherapp.data.utils.Result
import kotlin.math.roundToInt

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ForecastList(
    result: Result.Success<WeatherResponse>,
    ctx: Context
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color.LightGray.copy(alpha = .2f))
    ) {
        ForecastFor7DaysTitle()
        for (forecastDay in result.data.forecast.forecastday) {
            ForecastItem(forecastDay, result, ctx)
        }
    }
}

@Composable
private fun ForecastFor7DaysTitle() {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 8.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(16.dp),
                painter = painterResource(id = R.drawable.baseline_calendar_month_24),
                contentDescription = null,
                tint = Color.DarkGray
            )
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = "Прогноз на 7 дней",
                fontSize = 12.sp,
                color = Color.DarkGray
            )
        }
        HorizontalDivider(
            Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = Color.LightGray.copy(alpha = .3f)
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun ForecastItem(
    forecastDay: ForecastDay,
    result: Result.Success<WeatherResponse>,
    ctx: Context
) {
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                Modifier.fillMaxWidth(.5f),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = dateDisplay(date = forecastDay.date),
                    maxLines = 1,
                    textAlign = TextAlign.Start
                )
                AsyncImage(
                    modifier = Modifier
                        .size(48.dp)
                        ,
                    model = ImageRequest.Builder(ctx)
                        .data("https:${forecastDay.day.condition.icon}")
                        .size(Size.ORIGINAL)
                        .build(),
                    contentDescription = null
                )
            }

            Row(
                modifier = Modifier.weight(.5f),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.weight(.3f),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "${forecastDay.day.mintemp_c.roundToInt()}°",
                        maxLines = 1
                    )
                }
                TemperatureGradientBox(
                    currentTemperature = result.data.current.temp_c,
                    minTemperature = forecastDay.day.mintemp_c,
                    maxTemperature = forecastDay.day.maxtemp_c
                )
                Box(
                    modifier = Modifier.weight(.3f),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = "${forecastDay.day.maxtemp_c.roundToInt()}°", maxLines = 1)
                }
            }
        }
        HorizontalDivider(
            Modifier.fillMaxWidth(.9f),
            thickness = 1.dp,
            color = Color.LightGray.copy(alpha = .3f)
        )
    }
}