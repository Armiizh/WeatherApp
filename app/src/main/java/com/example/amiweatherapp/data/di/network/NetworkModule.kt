package com.example.amiweatherapp.data.di.network


import com.example.amiweatherapp.data.service.Service
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private val API_KEY = "094f8efd809ddffb67ce9f8309c7ec0d"


    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addNetworkInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
//            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .baseUrl("http://api.weatherapi.com/v1")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    fun provideService(retrofit: Retrofit): Service {
        return retrofit.create(Service::class.java)
    }
}