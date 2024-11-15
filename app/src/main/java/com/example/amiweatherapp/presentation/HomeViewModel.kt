package com.example.amiweatherapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.amiweatherapp.data.local.model.WeatherResponse
import com.example.amiweatherapp.domain.usecases.FetchWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fetchWeatherUseCase: FetchWeatherUseCase
): ViewModel() {

    private val _weatherData = MutableStateFlow<FetchWeatherUseCase.Result<WeatherResponse>?>(null)
    val weatherData: StateFlow<FetchWeatherUseCase.Result<WeatherResponse>?> get() = _weatherData

    fun fetchWeather(city: String? = null, lat: Double? = null, lon: Double? = null) {
        viewModelScope.launch {
            _weatherData.value = fetchWeatherUseCase.invoke(city, lat, lon)
        }
    }
}