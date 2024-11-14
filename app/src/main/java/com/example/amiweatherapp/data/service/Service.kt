package com.example.amiweatherapp.data.service

import com.example.amiweatherapp.data.local.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Service {

    @GET("weather")
    suspend fun fetchWeatherForCity(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("lang") lang: String,
        @Query("units") units: String
    ): Response<WeatherResponse>

    @GET("weather")
    suspend fun fetchLocalWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("lang") lang: String,
        @Query("units") units: String
    ): Response<WeatherResponse>
}