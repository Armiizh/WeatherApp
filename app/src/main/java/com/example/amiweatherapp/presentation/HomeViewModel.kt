package com.example.amiweatherapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.amiweatherapp.data.local.model.WeatherResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.amiweatherapp.data.utils.Result
import com.example.amiweatherapp.domain.usecases.FetchWeatherUseCase


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fetchWeatherUseCase: FetchWeatherUseCase
) : ViewModel() {

    private val _weatherData = MutableStateFlow<Result<WeatherResponse>?>(null)
    val weatherData: StateFlow<Result<WeatherResponse>?> get() = _weatherData


    private val _dataIsLoading = MutableStateFlow(true)
    val dataIsLoading: StateFlow<Boolean> = _dataIsLoading

    private var _lat: String? = null
    private var _lon: String? = null

    fun setCoordinates(lat: String?, lon: String?) {
        _lat = lat
        _lon = lon
    }

    private fun getCoordinates(): Pair<String?, String?> {
        return _lat to _lon
    }

    suspend fun fetchForecast(city: String? = null) {
        viewModelScope.launch {
            //Используем координаты, если город не передан
            val (lat, lon) = getCoordinates()
            _weatherData.value = fetchWeatherUseCase.invoke(city, lat, lon)
            _dataIsLoading.value = false
        }
    }
}