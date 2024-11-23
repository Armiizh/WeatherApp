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

    // Метод для получения всех данных о прогнозе погоды
    @Query("SELECT * FROM weather_response")
    suspend fun getAllWeatherResponses(): List<WeatherResponse>

    // Метод для удаления всех данных о прогнозе погоды (если это необходимо)
    @Query("DELETE FROM weather_response")
    suspend fun deleteAllWeatherResponses()
}