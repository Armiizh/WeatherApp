package com.example.amiweatherapp.data.local.converters

import androidx.room.TypeConverter
import com.example.amiweatherapp.data.local.model.CurrentWeather
import com.example.amiweatherapp.data.local.model.Day
import com.example.amiweatherapp.data.local.model.Forecast
import com.example.amiweatherapp.data.local.model.ForecastDay
import com.example.amiweatherapp.data.local.model.Hour
import com.example.amiweatherapp.data.local.model.Location
import com.example.amiweatherapp.data.local.model.WeatherCondition
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    private val gson = Gson()

    // Конвертер для CurrentWeather
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

    // Конвертер для Forecast
    @TypeConverter
    fun fromForecast(forecast: Forecast?): String? {
        return gson.toJson(forecast)
    }

    @TypeConverter
    fun toForecast(forecastString: String?): Forecast? {
        val forecastType = object : TypeToken<Forecast>() {}.type
        return gson.fromJson(forecastString, forecastType)
    }

    // Конвертер для списка ForecastDay
    @TypeConverter
    fun fromForecastDayList(forecastDayList: List<ForecastDay>?): String? {
        return forecastDayList?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toForecastDayList(forecastDayListString: String?): List<ForecastDay>? {
        return forecastDayListString?.let {
            val type = object : TypeToken<List<ForecastDay>>() {}.type
            gson.fromJson(it, type)
        }
    }

    // Конвертер для Day
    @TypeConverter
    fun fromDay(day: Day?): String? {
        return day?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toDay(dayString: String?): Day? {
        return dayString?.let {
            val type = object : TypeToken<Day>() {}.type
            gson.fromJson(it, type)
        }
    }

    // Конвертер для Hour
    @TypeConverter
    fun fromHour(hour: Hour?): String? {
        return hour?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toHour(hourString: String?): Hour? {
        return hourString?.let {
            val type = object : TypeToken<Hour>() {}.type
            gson.fromJson(it, type)
        }
    }

    // Конвертер для WeatherCondition
    @TypeConverter
    fun fromWeatherCondition(condition: WeatherCondition?): String? {
        return condition?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toWeatherCondition(conditionString: String?): WeatherCondition? {
        return conditionString?.let {
            val type = object : TypeToken<WeatherCondition>() {}.type
            gson.fromJson(it, type)
        }
    }

    // Конвертер для Location
    @TypeConverter
    fun fromLocation(location: Location?): String? {
        return location?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toLocation(locationString: String?): Location? {
        return locationString?.let {
            val type = object : TypeToken<Location>() {}.type
            gson.fromJson(it, type)
        }
    }
}