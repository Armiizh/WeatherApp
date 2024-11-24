package com.example.amiweatherapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.amiweatherapp.data.local.model.WeatherResponse

@Dao
interface WeatherDao {

    // Метод для вставки данных о прогнозе погоды
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeatherResponse(weatherResponse: WeatherResponse)
}