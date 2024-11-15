package com.example.amiweatherapp.presentation.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.amiweatherapp.domain.usecases.FetchWeatherUseCase
import com.example.amiweatherapp.presentation.HomeViewModel
import com.example.amiweatherapp.presentation.LocationViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = viewModel(),
    locationViewModel: LocationViewModel = viewModel()
) {
    var city by remember { mutableStateOf("") }
    val weatherData by homeViewModel.weatherData.collectAsState()
    val localCityName by locationViewModel.localCity.collectAsState()
    val location by locationViewModel.currentLocation.collectAsState()
    var isRefreshing by remember { mutableStateOf(false) }
    val swipeRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            Log.d("HomeScreen", "Refreshing weather data...")
            homeViewModel.viewModelScope.launch {
                try {
                    homeViewModel.fetchWeather(lat = location?.latitude, lon = location?.longitude)
                } catch (e: Exception) {
                    Log.e("HomeScreen", "Error fetching weather: ${e.message}")
                } finally {
                    isRefreshing = false // Устанавливаем обратно в false после завершения загрузки
                }
            }
        }
    )

    if (location != null) {
        LaunchedEffect(location) {
            homeViewModel.fetchWeather(lat = location?.latitude, lon = location?.longitude)
        }
    }

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
                    .pullRefresh(swipeRefreshState)
            ) {
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (location == null) {

                        Text(text = "Определение вашего местоположения...")
                        CircularProgressIndicator()

                    } else {

                        if (!isTextFieldVisible) {
                            Text(
                                text = localCityName,
                                fontSize = 36.sp
                            )
                            Text(
                                text = "${location?.latitude ?: ""}, ${location?.longitude ?: ""}",
                                fontSize = 12.sp
                            )
                            val ctx = LocalContext.current
                            when (val result = weatherData) {
                                is FetchWeatherUseCase.Result.Success -> {
                                    val iconUrl =
                                        result.data.weather.firstOrNull()?.icon.let { "http://openweathermap.org/img/wn/$it@2x.png" }
                                    Row(
                                        Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = "${result.data.main.temp}°",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 64.sp
                                        )
                                        AsyncImage(
                                            model = ImageRequest.Builder(ctx)
                                                .data(iconUrl)
                                                .size(Size.ORIGINAL)
                                                .build(),
                                            contentDescription = null
                                        )
//                                        Image(
//                                            painter = rememberAsyncImagePainter(iconUrl),
//                                            contentDescription = "Weather Icon",
//                                            modifier = Modifier.size(72.dp)
//                                        )
                                    }
                                    Text(text = "${result.data.weather.firstOrNull()?.description}")
                                    Text(
                                        text = "Мин.: ${result.data.main.temp_min}, Макс.: ${result.data.main.temp_max}",
                                        fontSize = 12.sp
                                    )
                                    Text(
                                        text = "Ощущается как ${result.data.main.feels_like}",
                                        fontSize = 12.sp
                                    )
                                }

                                is FetchWeatherUseCase.Result.Error -> {
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