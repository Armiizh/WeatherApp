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

    //Долгота и широта по геолокации
    private var _lat: String? = null
    private var _lon: String? = null

    //Методы для изменения геолокации
    fun setCoordinates(lat: String?, lon: String?) {
        _lat = lat
        _lon = lon
    }

    private fun getCoordinates(): Pair<String?, String?> {
        return _lat to _lon
    }

    private val _code = MutableStateFlow(0)
    val code: StateFlow<Int> get() = _code

    //Состояния для переключателей
    private val _isSpeedInMph = MutableStateFlow(false)
    val isSpeedInMph: StateFlow<Boolean> get() = _isSpeedInMph
    private val _isTempInFahrenheit = MutableStateFlow(false)
    val isTempInFahrenheit: StateFlow<Boolean> get() = _isTempInFahrenheit
    private val _isVisibilityInMiles = MutableStateFlow(false)
    val isVisibilityInMiles: StateFlow<Boolean> get() = _isVisibilityInMiles
    private val _isPressureInInch = MutableStateFlow(false)
    val isPressureInInch: StateFlow<Boolean> get() = _isPressureInInch

    //Методы для изменения состояний
    fun setSpeedInMph(value: Boolean) {
        _isSpeedInMph.value = value
    }

    fun setTempInFahrenheit(value: Boolean) {
        _isTempInFahrenheit.value = value
    }

    fun setPressureInInch(value: Boolean) {
        _isPressureInInch.value = value
    }

    fun setVisibilityInMiles(value: Boolean) {
        _isVisibilityInMiles.value = value
    }


    suspend fun fetchForecast(city: String? = null) {
        viewModelScope.launch {
            val (lat, lon) = getCoordinates()
            _weatherData.value = fetchWeatherUseCase.invoke(city, lat, lon) {
                _code.value = it
            }
            _dataIsLoading.value = false
        }
    }
}