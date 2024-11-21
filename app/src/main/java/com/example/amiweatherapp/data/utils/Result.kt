package com.example.amiweatherapp.data.utils

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val error: WeatherError) : Result<Nothing>()
}