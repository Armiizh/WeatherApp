package com.example.amiweatherapp.data.service

import com.example.amiweatherapp.data.local.model.ForecastFor7DaysResponse
import com.example.amiweatherapp.data.local.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Service {

    @GET("current.json")
    suspend fun fetchWeather(
        @Query("key") apiKey: String,
        @Query("q") latAndLon: String,
        @Query("aqi") aqi: String,
        @Query("lang") lang: String,
    ): Response<WeatherResponse>

    @GET("forecast.json")
    suspend fun forecast7Days(
        @Query("key") apiKey: String,
        @Query("q") latAndLon: String,
        @Query("days") days: String,
        @Query("aqi") aqi: String,
        @Query("lang") lang: String,
    ): Response<ForecastFor7DaysResponse>
}