package com.example.amiweatherapp.data.utils

sealed class WeatherError {
    object NetworkError : WeatherError()
    object DataNotFound : WeatherError()
    object ParsingError : WeatherError()
    data class UnknownError(val message: String) : WeatherError()
}