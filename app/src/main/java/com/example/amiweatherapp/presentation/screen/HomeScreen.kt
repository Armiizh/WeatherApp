package com.example.amiweatherapp.presentation.screen

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size
import com.example.amiweatherapp.data.local.model.ForecastDay
import com.example.amiweatherapp.data.local.model.ForecastFor7DaysResponse
import com.example.amiweatherapp.data.local.model.WeatherResponse
import com.example.amiweatherapp.presentation.HomeViewModel
import kotlinx.coroutines.launch
import com.example.amiweatherapp.data.utils.Result

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = viewModel(),
) {
    var city by remember { mutableStateOf("") }
    val weatherData by homeViewModel.weatherData.collectAsState()
    val forecastFor7DaysData by homeViewModel.forecastFor7DaysData.collectAsState()
    val dataIsLoading by homeViewModel.weatherDataIsLoading.collectAsState()
    val forecastFor7DaysIsLoading by homeViewModel.forecastFor7DaysIsLoading.collectAsState()

    var isTextFieldVisible by remember { mutableStateOf(false) }

    GradientBackground()
    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "AmiWeatherApp")
                        IconButton(
                            onClick = { isTextFieldVisible = !isTextFieldVisible }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "SearchIcon"
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (forecastFor7DaysIsLoading) {
                        Text(text = "Определение вашего местоположения...")
                        CircularProgressIndicator()
                    } else {
                        if (!isTextFieldVisible) {
                            val ctx = LocalContext.current

                            when (val result = forecastFor7DaysData) {
                                is Result.Success -> {
                                    TopSection(result, ctx)
                                    ForecastList(forecastFor7Days = result.data.forecast.forecastday)
                                    ReatTimeInfo(result)
                                }

                                is Result.Error -> {
                                    Toast.makeText(
                                        ctx,
                                        "Ошибка: ${result.error}",
                                        Toast.LENGTH_LONG
                                    )
                                        .show()
                                    Log.d("CHECK", "FetchWeatreUseCase ошибка - ${result.error}")
                                }

                                null -> {
                                    Text(text = "Введите город и нажмите 'Найти'")
                                }
                            }

//                            when (val result = weatherData) {
//                                is Result.Success -> {
//
//                                    TopSection(result, ctx)
//                                    ReatTimeInfo(result)
//                                }
//
//                                is Result.Error -> {
//                                    Toast.makeText(
//                                        ctx,
//                                        "Ошибка: ${result.error}",
//                                        Toast.LENGTH_LONG
//                                    )
//                                        .show()
//                                    Log.d("CHECK", "FetchWeatreUseCase ошибка - ${result.error}")
//                                }
//
//                                null -> {
//                                    Text(text = "Введите город и нажмите 'Найти'")
//                                }
//                            }


                        } else {

                            TextField(
                                modifier = Modifier
                                    .wrapContentWidth(),
                                value = city,
                                onValueChange = {
                                    city = it
                                },
                                placeholder = { Text(text = "Москва...", fontSize = 24.sp) },
                                colors = TextFieldDefaults.colors(
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    disabledContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedContainerColor = Color.Transparent
                                ),
                                trailingIcon = {
                                    if (city.length > 1) {
                                        IconButton(
                                            onClick = {
                                                homeViewModel.viewModelScope.launch {
                                                    homeViewModel.fetchWeather(
                                                        city
                                                    )
                                                }
                                                homeViewModel.viewModelScope.launch {
                                                    homeViewModel.fetchForecast(
                                                        city
                                                    )
                                                }
                                            }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Search,
                                                contentDescription = "SearchIcon"
                                            )
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
private fun ReatTimeInfo(result: Result.Success<ForecastFor7DaysResponse>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(color = Color.LightGray.copy(alpha = .2f))
            .shadow(elevation = 16.dp)
    ) {
        Text(
            modifier = Modifier.padding(start = 16.dp, top = 16.dp),
            text = result.data.current.condition.text
        )
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = "Ощущается как ${result.data.current.feelsLikeC}°",
            fontSize = 12.sp
        )
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = "Скорость ветра ${result.data.current.windKph} км/ч",
            fontSize = 12.sp
        )
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = "Порывы ветра до ${result.data.current.gustKph} км/ч",
            fontSize = 12.sp
        )
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = "Направление ветра ${result.data.current.windDirection}",
            fontSize = 12.sp
        )
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = "Влажность ${result.data.current.humidity}%",
            fontSize = 12.sp
        )
    }
}

@Composable
private fun TopSection(
    result: Result.Success<ForecastFor7DaysResponse>,
    ctx: Context
) {
    Text(
        text = result.data.location.name,
        fontSize = 36.sp
    )
    Text(
        text = "${result.data.location.region}, ${result.data.location.country}",
        fontSize = 12.sp
    )
    Text(
        text = "${result.data.location.lat}, ${result.data.location.lon}",
        fontSize = 12.sp
    )
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        val iconUrl =
            result.data.current.condition.icon
        AsyncImage(
            modifier = Modifier.size(64.dp),
            model = ImageRequest.Builder(ctx)
                .data("https:$iconUrl")
                .size(Size.ORIGINAL)
                .build(),
            contentDescription = null
        )
        Text(
            text = "${result.data.current.temperatureC}°",
            fontWeight = FontWeight.Bold,
            fontSize = 64.sp
        )
    }
}

@Composable
fun ForecastList(forecastFor7Days: List<ForecastDay>) {
    LazyColumn {
        items(forecastFor7Days) { forecastDay ->
            ForecastItem(forecastDay)
        }
    }
}

@Composable
fun ForecastItem(forecastDay: ForecastDay) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.LightGray.copy(alpha = 0.2f))
            .padding(16.dp)
    ) {
        Text(text = forecastDay.date, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(
            text = "Макс: ${forecastDay.day.maxtemp_c}°C, Мин: ${forecastDay.day.mintemp_c}°C",
            fontSize = 16.sp
        )
        Text(text = "Условия: ${forecastDay.day.condition.text}", fontSize = 16.sp)
        Text(text = "Вероятность дождя: ${forecastDay.day.daily_chance_of_rain}%", fontSize = 14.sp)
        Text(text = "Вероятность снега: ${forecastDay.day.daily_chance_of_snow}%", fontSize = 14.sp)
    }
}


@Composable
private fun GradientBackground() {
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