package com.example.amiweatherapp.data.local.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CurrentWeatherTypeConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromCurrentWeather(currentWeather: CurrentWeather?): String? {
        return currentWeather?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toCurrentWeather(currentWeatherString: String?): CurrentWeather? {
        return currentWeatherString?.let {
            val type = object : TypeToken<CurrentWeather>() {}.type
            gson.fromJson(it, type)
        }
    }
}