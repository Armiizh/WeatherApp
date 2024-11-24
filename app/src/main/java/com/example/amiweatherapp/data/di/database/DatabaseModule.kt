package com.example.amiweatherapp.data.di.database

import android.content.Context
import androidx.room.Room
import com.example.amiweatherapp.data.local.WeatherDatabase
import com.example.amiweatherapp.data.local.dao.WeatherDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun provideForecastFor7DaysDatabase(@ApplicationContext appContext: Context): WeatherDatabase {
        return Room.databaseBuilder(
            appContext,
            WeatherDatabase::class.java,
            "weather_response"
        )
            .build()
    }

    @Provides
    fun provideForecastFor7DaysDao(database: WeatherDatabase): WeatherDao {
        return database.dao()
    }
}