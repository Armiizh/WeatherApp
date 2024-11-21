package com.example.amiweatherapp.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "weather_resp")
data class WeatherResponse(
    @PrimaryKey
    val id: Int = 0,
    val location: Location,
    val current: CurrentWeather
)