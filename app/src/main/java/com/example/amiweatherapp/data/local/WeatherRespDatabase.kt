package com.example.amiweatherapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.amiweatherapp.data.local.dao.WeatherRespDao
import com.example.amiweatherapp.data.local.model.RespConv
import com.example.amiweatherapp.data.local.model.WeatherResp

@Database(entities = [WeatherResp::class], version = 1)
@TypeConverters(RespConv::class)
abstract class WeatherRespDatabase : RoomDatabase() {
    abstract fun weatherRespDao(): WeatherRespDao
}