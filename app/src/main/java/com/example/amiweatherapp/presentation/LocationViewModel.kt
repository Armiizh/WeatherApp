package com.example.amiweatherapp.presentation

import android.content.Context
import android.location.Geocoder
import android.location.Location
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor() : ViewModel() {

    private val _currentLocation = MutableStateFlow<Location?>(null)
    val currentLocation: StateFlow<Location?> = _currentLocation

    private val _localCity = MutableStateFlow<String>("")
    val localCity: StateFlow<String> = _localCity

    fun updateLocation(location: Location, ctx: Context) {
        viewModelScope.launch {
            _currentLocation.value = location
            val cityName = getCityName(location, ctx)
            _localCity.value = cityName
        }
    }

    private fun getCityName(location: Location, ctx: Context): String {
        val geocoder = Geocoder(ctx, Locale("ru"))
        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)

        return if (!addresses.isNullOrEmpty()) {
            addresses[0].locality ?: addresses[0].adminArea ?: "Не удалось получить название города"
        } else {
            "Не удалось получить название города"
        }
    }
}