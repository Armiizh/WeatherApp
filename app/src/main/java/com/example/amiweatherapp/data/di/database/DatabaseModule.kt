package com.example.amiweatherapp.data.di.database

import android.content.Context
import androidx.room.Room
import com.example.amiweatherapp.data.local.ForecastFor7DaysDatabase
import com.example.amiweatherapp.data.local.dao.ForecastFor7DaysDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

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