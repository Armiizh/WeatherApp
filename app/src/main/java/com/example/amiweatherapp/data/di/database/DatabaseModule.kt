package com.example.amiweatherapp.data.di.database

import android.content.Context
import androidx.room.Room
import com.example.amiweatherapp.data.local.ForecastFor7DaysDatabase
import com.example.amiweatherapp.data.local.WeatherResponseDatabase
import com.example.amiweatherapp.data.local.dao.ForecastFor7DaysDao
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
    fun provideWeatherRespDatabase(@ApplicationContext appContext: Context): WeatherResponseDatabase {
        return Room.databaseBuilder(appContext, WeatherResponseDatabase::class.java, "weather_resp")
            .build()
    }

    @Provides
    fun provideWeatherRespDao(weatherResponseDatabase: WeatherResponseDatabase): WeatherResponseDao {
        return weatherResponseDatabase.weatherResponseDao()
    }

    @Provides
    fun provideForecastFor7DaysDatabase(@ApplicationContext appContext: Context): ForecastFor7DaysDatabase {
        return Room.databaseBuilder(
            appContext,
            ForecastFor7DaysDatabase::class.java,
            "forecast_for_7_days"
        )
            .build()
    }

    @Provides
    fun provideForecastFor7DaysDao(forecastFor7DaysDatabase: ForecastFor7DaysDatabase): ForecastFor7DaysDao {
        return forecastFor7DaysDatabase.forecastFor7DaysDao()
    }
}