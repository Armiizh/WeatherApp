package com.example.amiweatherapp.data.local.model

import androidx.room.TypeConverter
import com.google.gson.Gson

class RespConv {

    @TypeConverter
    fun fromJson(json: String): WeatherResponse {
        val gson = Gson()
        return gson.fromJson(json, WeatherResponse::class.java)
    }
    @TypeConverter
    fun toJson(weatherResponse: WeatherResponse): String {
        val gson = Gson()
        return gson.toJson(weatherResponse)
    }
}