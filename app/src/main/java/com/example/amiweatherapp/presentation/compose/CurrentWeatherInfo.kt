package com.example.amiweatherapp.presentation.compose

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.amiweatherapp.R
import com.example.amiweatherapp.data.local.model.ForecastFor7DaysResponse
import com.example.amiweatherapp.data.utils.Result
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CurrentWeatherInfo(result: Result.Success<ForecastFor7DaysResponse>) {

    val currentDateTime = LocalDateTime.now()
    val currentHour = currentDateTime.format(DateTimeFormatter.ofPattern("HH"))
    val currentDate = LocalDate.now()

    val todayForecast = result.data.forecast.forecastday.find { it.date == currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) }
    val currentHourForecast = todayForecast?.hour?.find { it.time.contains(currentHour) }

    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        //Первая строка
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            //Первый виджет
            AmiWeatherWidget(
                Modifier
                    .weight(1f)
                    .padding(end = 4.dp)
            ) {
                WidgetTitle(title = "ОЩУЩАЕТСЯ КАК", iconId = R.drawable.ic_temperature_fiill_like)
                SmallText(
                    Modifier.padding(horizontal = 16.dp),
                    "${result.data.current.feelslike_c.roundToInt()}°"
                )
            }
            //Второй виджет
            AmiWeatherWidget(
                Modifier
                    .weight(1f)
                    .padding(end = 4.dp)
            ) {
                val pressure =
                    if (result.data.current.pressure_mb != null && result.data.current.pressure_mb.roundToInt() != 0) {
                        "${result.data.current.pressure_mb.roundToInt()} мм рт. ст."
                    } else {
                        "${inchesToMillimetersHg(result.data.current.pressure_in).roundToInt()} мм рт. ст."
                    }
                WidgetTitle(title = "ДАВЛЕНИЕ", iconId = R.drawable.ic_pressure)
                SmallText(
                    Modifier.padding(horizontal = 16.dp),
                    text = pressure
                )
            }
        }
        //Вторая строка
        Row(
            Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            //Третий виджет
            AmiWeatherWidget(
                Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp)
            ) {
                WidgetTitle(title = "ВЕТЕР", iconId = R.drawable.ic_wind)
                Column(Modifier.padding(horizontal = 16.dp)) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Ветер")
                        Text(text = "${result.data.current.wind_kph.roundToInt()} км/ч")
                    }
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Порывы ветра")
                        Text(text = "${result.data.current.gust_kph.roundToInt()} км/ч")
                    }
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Направление")
                        Text(text = "${result.data.current.wind_degree}° ${result.data.current.wind_dir}")
                    }
                }
            }
        }
        //Третья строка
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            //Первый виджет
            AmiWeatherWidget(
                Modifier
                    .weight(1f)
                    .padding(end = 4.dp)
            ) {
                WidgetTitle(title = "ВЛАЖНОСТЬ", iconId = R.drawable.ic_humidity)
                SmallText(
                    Modifier.padding(horizontal = 16.dp),
                    "${result.data.current.humidity}%"
                )
            }
            //Второй виджет
            AmiWeatherWidget(
                Modifier
                    .weight(1f)
                    .padding(start = 4.dp)
            ) {
                WidgetTitle(title = "ВИДИМОСТЬ", iconId = R.drawable.ic_visibility)
                SmallText(
                    Modifier.padding(horizontal = 16.dp),
                    "${currentHourForecast?.vis_km?.roundToInt() ?: "N/A"} км"
                )
            }
        }
    }
}

fun inchesToMillimetersHg(inches: Double): Double {
    return inches * 25.4
}

@Composable
fun SmallText(modifier: Modifier = Modifier, text: String) {
    Text(
        modifier = modifier,
        text = text
    )
}

@Composable
fun WidgetTitle(title: String, iconId: Int) {
    Row(
        modifier = Modifier.padding(start = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(16.dp),
            painter = painterResource(iconId),
            contentDescription = null,
            tint = Color.DarkGray
        )
        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = title,
            fontSize = 12.sp,
            color = Color.DarkGray
        )
    }
}

@Composable
fun AmiWeatherWidget(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color.LightGray.copy(alpha = .2f))
            .padding(8.dp) // Добавляем немного внутреннего отступа
    ) {
        content()
    }
}