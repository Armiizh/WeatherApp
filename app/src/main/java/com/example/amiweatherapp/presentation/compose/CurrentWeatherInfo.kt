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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.amiweatherapp.R
import com.example.amiweatherapp.data.local.model.WeatherResponse
import com.example.amiweatherapp.data.utils.Result
import com.example.amiweatherapp.presentation.HomeViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CurrentWeatherInfo(viewModel: HomeViewModel, result: Result.Success<WeatherResponse>) {

    val currentDateTime = LocalDateTime.now()
    val currentHour = currentDateTime.format(DateTimeFormatter.ofPattern("HH"))
    val currentDate = LocalDate.now()
    val todayForecast = result.data.forecast.forecastday.find {
        it.date == currentDate.format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd")
        )
    }
    val currentHourForecast = todayForecast?.hour?.find { it.time.contains(currentHour) }
    val isTempInFahrenheit by viewModel.isTempInFahrenheit.collectAsState()
    val isPressureInInch by viewModel.isPressureInInch.collectAsState()
    val isSpeedInMph by viewModel.isSpeedInMph.collectAsState()
    val isVisibilityInMiles by viewModel.isVisibilityInMiles.collectAsState()

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
                    text = if (isTempInFahrenheit) {
                        "${result.data.current.feelslike_f.roundToInt()}°"
                    } else {
                        "${result.data.current.feelslike_c.roundToInt()}°"
                    },
                )
            }
            //Второй виджет
            AmiWeatherWidget(
                Modifier
                    .weight(1f)
                    .padding(end = 4.dp)
            ) {
                var pressure =
                    if (result.data.current.pressure_mb != null && result.data.current.pressure_mb.roundToInt() != 0) {
                        "${result.data.current.pressure_mb.roundToInt()} мм рт. ст."
                    } else {
                        "${inchesToMillimetersHg(result.data.current.pressure_in).roundToInt()} мм рт. ст."
                    }
                if (isPressureInInch) {
                    pressure =
                        "${result.data.current.pressure_in.roundToInt()} дюйм рт. ст."
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
            Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            //Третий виджет
            AmiWeatherWidget(
                Modifier.weight(1f)
            ) {
                WidgetTitle(title = "ВЕТЕР", iconId = R.drawable.ic_wind)
                Column(Modifier.padding(horizontal = 16.dp)) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Ветер",color = Color.White)
                        Text(
                            text = if (isSpeedInMph) {
                                "${result.data.current.wind_mph.roundToInt()} миль/ч"
                            } else {
                                "${result.data.current.wind_kph.roundToInt()} км/ч"
                            },
                            color = Color.White
                        )
                    }
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Порывы ветра",color = Color.White)
                        Text(
                            text = if (isSpeedInMph) {
                                "${result.data.current.gust_mph.roundToInt()} миль/ч"
                            } else {
                                "${result.data.current.gust_kph.roundToInt()} км/ч"
                            },
                            color = Color.White

                        )
                    }
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Направление",color = Color.White)
                        Text(
                            text = "${result.data.current.wind_degree}° ${result.data.current.wind_dir}",
                            color = Color.White
                        )
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
                    text = if (isVisibilityInMiles) {
                        "${currentHourForecast?.vis_miles?.roundToInt() ?: "N/A"} миль"
                    } else {
                        "${currentHourForecast?.vis_km?.roundToInt() ?: "N/A"} км"
                    }
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
        text = text,
        color = Color.White
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
            tint = Color.White
        )
        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = title,
            fontSize = 12.sp,
            color = Color.White
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
            .padding(8.dp)
    ) {
        content()
    }
}