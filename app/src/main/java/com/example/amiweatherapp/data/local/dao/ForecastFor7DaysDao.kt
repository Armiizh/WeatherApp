package com.example.amiweatherapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.amiweatherapp.data.local.model.ForecastFor7DaysResponse

@Dao
interface ForecastFor7DaysDao {

    // Метод для вставки данных о прогнозе погоды
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeatherResponse(forecastFor7DaysResponse: ForecastFor7DaysResponse)

    // Метод для получения всех данных о прогнозе погоды
    @Query("SELECT * FROM forecast_for_7_days")
    suspend fun getAllWeatherResponses(): List<ForecastFor7DaysResponse>

    // Метод для удаления всех данных о прогнозе погоды (если это необходимо)
    @Query("DELETE FROM forecast_for_7_days")
    suspend fun deleteAllWeatherResponses()
}