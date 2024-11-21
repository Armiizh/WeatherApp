package com.example.amiweatherapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.amiweatherapp.data.local.dao.ForecastFor7DaysDao
import com.example.amiweatherapp.data.local.model.CurrentWeatherTypeConverter
import com.example.amiweatherapp.data.local.model.ForecastConverters
import com.example.amiweatherapp.data.local.model.ForecastFor7DaysResponse
import com.example.amiweatherapp.data.local.model.LocationTypeConverter

@Database(entities = [ForecastFor7DaysResponse::class], version = 1)
@TypeConverters(ForecastConverters::class, CurrentWeatherTypeConverter::class, LocationTypeConverter::class)
abstract class ForecastFor7DaysDatabase : RoomDatabase() {
    abstract fun forecastFor7DaysDao(): ForecastFor7DaysDao
}