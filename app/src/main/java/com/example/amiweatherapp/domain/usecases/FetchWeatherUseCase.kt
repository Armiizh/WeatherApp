package com.example.amiweatherapp.domain.usecases

import android.util.Log
import com.example.amiweatherapp.data.local.dao.WeatherResponseDao
import com.example.amiweatherapp.data.local.model.CurrentWeather
import com.example.amiweatherapp.data.local.model.Location
import com.example.amiweatherapp.data.local.model.WeatherCondition
import com.example.amiweatherapp.data.local.model.WeatherResponse
import com.example.amiweatherapp.data.service.Service
import com.example.amiweatherapp.data.utils.WeatherError
import javax.inject.Inject
import com.example.amiweatherapp.data.utils.Result

class FetchWeatherUseCase @Inject constructor(
    private val service: Service,
    private val weatherResponseDao: WeatherResponseDao
) {
    suspend fun invoke(
        city: String? = null,
        lat: String? = null,
        lon: String? = null
    ): Result<WeatherResponse> {
        return try {
            val response = if (city != null) {
                Log.d(
                    "city",
                    "city = $city"
                )
                service.fetchWeather("e098f2674efa47a28e2171146241511", city, "no", "ru")
            } else if (lat != null && lon != null) {
                val q = "$lat,$lon"
                Log.d(
                    "CHECK",
                    "lat = $lat, lon = $lon"
                )
                service.fetchWeather("e098f2674efa47a28e2171146241511", q, "no", "ru")
            } else {
                Log.d(
                    "CHECK",
                    "Что то не так с ответом от сервера место: FetchWeatherUseCase.invoke.response"
                )
                throw IllegalArgumentException("Either city name or location coordinates must be provided.")
            }

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    val data = WeatherResponse(
                        location = Location(
                            name = body.location.name,
                            region = body.location.region,
                            country = body.location.country,
                            lat = body.location.lat,
                            lon = body.location.lon
                        ),
                        current = CurrentWeather(
                            condition = WeatherCondition(
                                text = body.current.condition.text,
                                icon = body.current.condition.icon
                            ),
                            temperatureC = body.current.temperatureC,
                            temperatureF = body.current.temperatureF,
                            feelsLikeC = body.current.feelsLikeC,
                            feelsLikeF = body.current.feelsLikeF,
                            windKph = body.current.windKph,
                            windMph = body.current.windMph,
                            windDirection = body.current.windDirection,
                            humidity = body.current.humidity,
                            gustKph = body.current.gustKph,
                            gustMph = body.current.gustMph
                        )
                    )
                    Log.d("CHECK", "data - $data")
                    weatherResponseDao.insertWeatherResponse(data)
                    Result.Success(data)
                } else {
                    Log.d(
                        "CHECK",
                        "Что то не так с ответом от сервера место: FetchWeatherUseCase.invoke.!isSuccesful.DATANOTFOUND"
                    )
                    Result.Error(WeatherError.DataNotFound)
                }
            } else {
                Log.d(
                    "CHECK",
                    "Что то не так с ответом от сервера место: FetchWeatherUseCase.invoke.!isSuccesful.NETWORK"
                )
                Result.Error(WeatherError.NetworkError)
            }
        } catch (e: Exception) {
            Log.d(
                "CHECK",
                "Что то не так с ответом от сервера место: FetchWeatherUseCase.invoke.!isSuccesful.Ecpection - ${e.stackTrace}\n${e.message}"
            )
            Result.Error(WeatherError.UnknownError(e.message ?: "Неизвестная ошибка"))
        }
    }
}