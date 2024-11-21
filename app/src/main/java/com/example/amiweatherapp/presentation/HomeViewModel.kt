package com.example.amiweatherapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.amiweatherapp.data.local.model.ForecastFor7DaysResponse
import com.example.amiweatherapp.data.local.model.WeatherResponse
import com.example.amiweatherapp.domain.usecases.FetchWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.amiweatherapp.data.utils.Result
import com.example.amiweatherapp.domain.usecases.FetchForecastFor7DaysUseCase


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fetchWeatherUseCase: FetchWeatherUseCase,
    private val fetchForecastFor7DaysUseCase: FetchForecastFor7DaysUseCase
) : ViewModel() {

    private val _weatherData = MutableStateFlow<Result<WeatherResponse>?>(null)
    private val _forecastFor7DaysData = MutableStateFlow<Result<ForecastFor7DaysResponse>?>(null)
    val weatherData: StateFlow<Result<WeatherResponse>?> get() = _weatherData
    val forecastFor7DaysData: StateFlow<Result<ForecastFor7DaysResponse>?> get() = _forecastFor7DaysData


    private val _weatherDataIsLoading = MutableStateFlow(true)
    private val _forecastFor7DaysIsLoading = MutableStateFlow(true)
    val weatherDataIsLoading: StateFlow<Boolean> = _weatherDataIsLoading
    val forecastFor7DaysIsLoading: StateFlow<Boolean> = _forecastFor7DaysIsLoading

    suspend fun fetchWeather(city: String? = null, lat: String? = null, lon: String? = null) {
        viewModelScope.launch {
            _weatherData.value = fetchWeatherUseCase.invoke(city, lat, lon)
            _weatherDataIsLoading.value = false
        }
    }
    suspend fun fetchForecast(city: String? = null, lat: String? = null, lon: String? = null) {
        viewModelScope.launch {
            _forecastFor7DaysData.value = fetchForecastFor7DaysUseCase.invoke(city, lat, lon)
            _forecastFor7DaysIsLoading.value = false
        }
    }
}