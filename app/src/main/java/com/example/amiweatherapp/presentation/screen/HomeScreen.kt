package com.example.amiweatherapp.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.amiweatherapp.domain.usecases.FetchLocalWeatherUseCase
import com.example.amiweatherapp.domain.usecases.FetchWeatherForCityUseCase
import com.example.amiweatherapp.presentation.HomeViewModel
import com.example.amiweatherapp.presentation.LocationViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    locationPermissionState: PermissionState,
    homeViewModel: HomeViewModel = viewModel(),
    locationViewModel: LocationViewModel = viewModel()
) {
    var city by remember { mutableStateOf("") }
    val weatherForCity by homeViewModel.weatherForCity.collectAsState()
    val localWeather by homeViewModel.localWeather.collectAsState()
    val location by locationViewModel.currentLocation.collectAsState()
    var isLocationLoading by remember { mutableStateOf(true) }
    var isFetchingLocalWeather by remember { mutableStateOf(true) } // Состояние для отображения данных

    if (location != null) {
        isLocationLoading = false
        if (isFetchingLocalWeather) {
            LaunchedEffect(location) {
                homeViewModel.fetchLocalWeather(location!!.latitude, location!!.longitude)
                isFetchingLocalWeather = false // Установите флаг в false после получения данных
            }
        }
    }
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
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "AmiWeatherApp") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when {

                    locationPermissionState.hasPermission -> {

                        if (location == null) {
                            Text(text = "Определение вашего местоположения...")
                            LinearProgressIndicator()
                        } else {
                            OutlinedTextField(
                                value = city,
                                onValueChange = {
                                    city = it
                                }
                            )
                            Button(onClick = {
                                homeViewModel.viewModelScope.launch {
                                    homeViewModel.fetchWeatherForCity(city)
                                }
                            }) {
                                Text(text = "Найти")
                            }

                            // Отображение местоположения, если оно доступно
                            Text(text = "Ваше местоположение: ${location?.latitude ?: "Нема"}, ${location?.longitude ?: "Нетю"}")

                            if (isFetchingLocalWeather) {
                                when (val result = localWeather) {
                                    is FetchLocalWeatherUseCase.Result.Success -> {
                                        Text(text = "Температура: ${result.data.main.temp}")
                                    }
                                    is FetchLocalWeatherUseCase.Result.Error -> {
                                        Text(text = "Ошибка: ${result.error}")
                                    }
                                    null -> {
                                        Text(text = "Загрузка местной погоды...")
                                    }
                                }
                            } else {
                                when (val result = weatherForCity) {
                                    is FetchWeatherForCityUseCase.Result.Success -> {
                                        Text(text = "Температура: ${result.data.main.temp}")
                                    }
                                    is FetchWeatherForCityUseCase.Result.Error -> {
                                        Text(text = "Ошибка: ${result.error}")
                                    }
                                    null -> {
                                        Text(text = "Введите город и нажмите 'Найти'")
                                    }
                                }
                            }
                        }
                    }

                    locationPermissionState.shouldShowRationale -> {
                        Text("Для работы приложения требуется доступ к вашему местоположению.")
                        Button(onClick = { locationPermissionState.launchPermissionRequest() }) {
                            Text("Запросить разрешение")
                        }
                    }

                    else -> {
                        Text("Разрешение на геолокацию отклонено. Пожалуйста, измените настройки приложения.")
                    }
                }

            }
        }
    )
}