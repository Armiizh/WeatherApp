package com.example.amiweatherapp.data.utils

sealed class WeatherError {
    object NetworkError : WeatherError()
    object DataNotFound : WeatherError()
    object ParsingError : WeatherError()
    data class UnknownError(val message: String) : WeatherError()
}

fun getErrorMessage(error: WeatherError): String {
    return when (error) {
        is WeatherError.NetworkError -> "Ошибка сети. Проверьте подключение к интернету."
        is WeatherError.DataNotFound -> "Данные не найдены. Попробуйте другой город."
        is WeatherError.ParsingError -> "Ошибка парсинга данных. Попробуйте снова."
        is WeatherError.UnknownError -> error.message
    }
}