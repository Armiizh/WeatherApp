package com.example.amiweatherapp.presentation.screen

import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.ColumnInfo
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size
import com.example.amiweatherapp.data.local.model.ForecastFor7DaysResponse
import com.example.amiweatherapp.data.local.model.Hour
import com.example.amiweatherapp.data.utils.Result
import com.example.amiweatherapp.presentation.HomeViewModel
import com.example.amiweatherapp.presentation.compose.ForecastList
import com.example.amiweatherapp.presentation.compose.GradientBackground
import com.example.amiweatherapp.presentation.compose.LoadingDataContent
import com.example.amiweatherapp.presentation.compose.TopSection
import com.example.amiweatherapp.presentation.compose.CurrentWeatherInfo
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = viewModel(),
) {
    var city by remember { mutableStateOf("") }
    val forecastFor7DaysData by homeViewModel.forecastFor7DaysData.collectAsState()
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
                            if (isTextFieldVisible) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "CloseIcon"
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "SearchIcon"
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        content = { paddingValues ->

            if (forecastFor7DaysIsLoading) {
                LoadingDataContent()
            } else {

                Column(
                    Modifier
                        .padding(paddingValues)
                        .fillMaxWidth(),
                    Arrangement.Center,
                    Alignment.CenterHorizontally
                ) {

                    if (!isTextFieldVisible) {
                        val ctx = LocalContext.current

                        when (val result = forecastFor7DaysData) {
                            is Result.Success -> {
                                Text(
                                    text = result.data.location.name,
                                    fontSize = 36.sp
                                )
                                Column(
                                    Modifier
                                        .fillMaxWidth()
                                        .verticalScroll(rememberScrollState()),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    TopSection(result, ctx)
                                    Spacer(Modifier.height(48.dp))
                                    HourlyForecast(result, ctx)
                                    Spacer(Modifier.height(8.dp))
                                    ForecastList(result, ctx)
                                    Spacer(Modifier.height(8.dp))
                                    CurrentWeatherInfo(result)
                                }
                            }

                            is Result.Error -> {
                                Toast.makeText(
                                    ctx,
                                    "Ошибка: ${result.error}",
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                                Log.d("CHECK", "FetchWeatherUseCase ошибка - ${result.error}")
                            }

                            null -> {
                                Text(text = "Введите город и нажмите 'Найти'")
                            }
                        }
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
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HourlyForecast(result: Result.Success<ForecastFor7DaysResponse>, ctx: Context) {

    val currentDateTime = LocalDateTime.now()
    val currentHour = currentDateTime.format(DateTimeFormatter.ofPattern("HH"))
    val currentDate = LocalDate.now()
    val todayForecast = result.data.forecast.forecastday.find { it.date == currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) }
    val tomorrowForecast = result.data.forecast.forecastday.find { it.date == currentDate.plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) }

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
