package com.example.amiweatherapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.amiweatherapp.data.local.model.WeatherResponse
import com.example.amiweatherapp.domain.usecases.FetchLocalWeatherUseCase
import com.example.amiweatherapp.domain.usecases.FetchWeatherForCityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fetchWeatherForCityUseCase: FetchWeatherForCityUseCase,
    private val fetchLocalWeatherUseCase: FetchLocalWeatherUseCase
): ViewModel() {

    private val _weatherForCity = MutableStateFlow<FetchWeatherForCityUseCase.Result<WeatherResponse>?>(null)
    private val _localWeather = MutableStateFlow<FetchLocalWeatherUseCase.Result<WeatherResponse>?>(null)
    val weatherForCity: StateFlow<FetchWeatherForCityUseCase.Result<WeatherResponse>?> get() = _weatherForCity
    val localWeather: StateFlow<FetchLocalWeatherUseCase.Result<WeatherResponse>?> get() = _localWeather

    fun fetchWeatherForCity(city: String) {
        viewModelScope.launch {
            _weatherForCity.value = fetchWeatherForCityUseCase.invoke(city)
        }
    }
    fun fetchLocalWeather(lat: Double, lon: Double) {
        viewModelScope.launch {
            _localWeather.value = fetchLocalWeatherUseCase.invoke(lat, lon)
        }
    }
}