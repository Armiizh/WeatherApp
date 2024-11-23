package com.example.amiweatherapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.amiweatherapp.data.local.dao.WeatherDao
import com.example.amiweatherapp.data.local.converters.Converters
import com.example.amiweatherapp.data.local.model.WeatherResponse

@Database(entities = [WeatherResponse::class], version = 1)
@TypeConverters(Converters::class)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun dao(): WeatherDao
}