package com.example.amiweatherapp.data.di.database

import android.content.Context
import androidx.room.Room
import com.example.amiweatherapp.data.local.WeatherResponseDatabase
import com.example.amiweatherapp.data.local.dao.WeatherResponseDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun provideWeatherResponseDatabase(@ApplicationContext appContext: Context): WeatherResponseDatabase {
        return Room.databaseBuilder(appContext, WeatherResponseDatabase::class.java, "weather_response")
            .build()
    }

    @Provides
    fun provideWeatherResponseDao(weatherResponseDatabase: WeatherResponseDatabase): WeatherResponseDao {
        return weatherResponseDatabase.weatherResponseDao()
    }
}