package com.example.amiweatherapp.domain.usecases

import android.util.Log
import com.example.amiweatherapp.data.local.dao.WeatherRespDao
import com.example.amiweatherapp.data.local.dao.WeatherResponseDao
import com.example.amiweatherapp.data.local.model.Clouds
import com.example.amiweatherapp.data.local.model.Coord
import com.example.amiweatherapp.data.local.model.Main
import com.example.amiweatherapp.data.local.model.Sys
import com.example.amiweatherapp.data.local.model.Weather
import com.example.amiweatherapp.data.local.model.WeatherResp
import com.example.amiweatherapp.data.local.model.WeatherResponse
import com.example.amiweatherapp.data.local.model.Wind
import com.example.amiweatherapp.data.service.Service
import com.example.amiweatherapp.data.utils.WeatherError
import javax.inject.Inject

class FetchWeatherUseCase @Inject constructor(
    private val service: Service,
    private val weatherResponseDao: WeatherResponseDao,
    private val weatherRespDao: WeatherRespDao
) {
    sealed class Result<out T> {
        data class Success<out T>(val data: T) : Result<T>()
        data class Error(val error: WeatherError) : Result<Nothing>()
    }


    //    e098f2674efa47a28e2171146241511
    suspend fun invoke(
        city: String? = null,
        lat: Double? = null,
        lon: Double? = null
    ): Result<WeatherResponse> {
        return try {
            val response = if (city != null) {
                service.fetchWeather("e098f2674efa47a28e2171146241511", city, "no")
//                service.fetchWeatherForCity(
//                    city,
//                    "094f8efd809ddffb67ce9f8309c7ec0dY",
//                    "ru",
//                    "metric"
//                )
            } else if (lat != null && lon != null) {
                val q = "$lat,$lon"
                service.fetchWeather("e098f2674efa47a28e2171146241511", q, "no")
//                service.fetchLocalWeather(
//                    lat,
//                    lon,
//                    "094f8efd809ddffb67ce9f8309c7ec0d",
//                    "ru",
//                    "metric"
//                )
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
                    val data = WeatherResp(

                    )
//                    val weatherData = WeatherResponse(
//                        coord = Coord(
//                            lon = body.coord.lon,
//                            lat = body.coord.lat
//                        ),
//                        weather = body.weather.map { weather ->
//                            Weather(
//                                id = weather.id,
//                                main = weather.main,
//                                description = weather.description,
//                                icon = weather.icon
//                            )
//                        },
//                        base = body.base,
//                        main = Main(
//                            temp = body.main.temp,
//                            feels_like = body.main.feels_like,
//                            temp_min = body.main.temp_min,
//                            temp_max = body.main.temp_max,
//                            pressure = body.main.pressure,
//                            humidity = body.main.humidity,
//                            sea_level = body.main.sea_level,
//                            grnd_level = body.main.grnd_level
//                        ),
//                        visibility = body.visibility,
//                        wind = Wind(
//                            speed = body.wind.speed,
//                            deg = body.wind.deg,
//                            gust = body.wind.gust
//                        ),
//                        clouds = Clouds(
//                            all = body.clouds.all
//                        ),
//                        dt = body.dt,
//                        sys = Sys(
//                            type = body.sys.type,
//                            id = body.sys.id,
//                            country = body.sys.country,
//                            sunrise = body.sys.sunrise,
//                            sunset = body.sys.sunset
//                        ),
//                        timezone = body.timezone,
//                        id = body.id,
//                        name = body.name,
//                        cod = body.cod
//                    )
                    Log.d("CHECK", "data - $weatherData")
                    weatherResponseDao.insertWeatherResponse(weatherData)
                    Result.Success(weatherData)
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