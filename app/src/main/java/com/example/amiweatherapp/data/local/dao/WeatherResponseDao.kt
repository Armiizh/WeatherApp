package com.example.amiweatherapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.amiweatherapp.data.local.model.WeatherResponse
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherResponseDao {

        // Вставка WeatherResponse в базу данных
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertWeatherResponse(weatherResponse: WeatherResponse)

        // Получение WeatherResponse по id
        @Query("SELECT * FROM weather_resp WHERE id = :id")
        suspend fun getWeatherResponseById(id: Int): WeatherResponse?

        // Получение всех WeatherResponse
        @Query("SELECT * FROM weather_resp")
        fun getAllWeatherResponses(): Flow<List<WeatherResponse>>

        // Удаление WeatherResponse по id
        @Query("DELETE FROM weather_resp WHERE id = :id")
        suspend fun deleteWeatherResponseById(id: Int)

        // Удаление всех WeatherResponse
        @Query("DELETE FROM weather_resp")
        suspend fun deleteAllWeatherResponses()
}