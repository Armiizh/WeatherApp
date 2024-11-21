package com.example.amiweatherapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.amiweatherapp.data.local.dao.WeatherResponseDao
import com.example.amiweatherapp.data.local.model.CurrentWeatherTypeConverter
import com.example.amiweatherapp.data.local.model.LocationTypeConverter
import com.example.amiweatherapp.data.local.model.WeatherResponse

@Database(entities = [WeatherResponse::class], version = 1)
@TypeConverters(CurrentWeatherTypeConverter::class, LocationTypeConverter::class)
abstract class WeatherResponseDatabase : RoomDatabase() {
    abstract fun weatherResponseDao(): WeatherResponseDao
}