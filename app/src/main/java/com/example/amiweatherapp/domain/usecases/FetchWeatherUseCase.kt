package com.example.amiweatherapp.domain.usecases

import android.util.Log
import com.example.amiweatherapp.data.local.WeatherDatabase
import com.example.amiweatherapp.data.local.model.CurrentWeather
import com.example.amiweatherapp.data.local.model.Day
import com.example.amiweatherapp.data.local.model.Forecast
import com.example.amiweatherapp.data.local.model.ForecastDay
import com.example.amiweatherapp.data.local.model.WeatherResponse
import com.example.amiweatherapp.data.local.model.Hour
import com.example.amiweatherapp.data.local.model.Location
import com.example.amiweatherapp.data.local.model.WeatherCondition
import com.example.amiweatherapp.data.service.Service
import com.example.amiweatherapp.data.utils.Result
import com.example.amiweatherapp.data.utils.WeatherError
import javax.inject.Inject

class FetchWeatherUseCase @Inject constructor(
    private val service: Service,
    private val database: WeatherDatabase
) {
    suspend fun invoke(
        city: String? = null,
        lat: String? = null,
        lon: String? = null
    ): Result<WeatherResponse> {
        return try {
            val response = if (city != null) {
                Log.d(
                    "CHECK",
                    "city = $city"
                )
                service.fetchWeather(
                    "e098f2674efa47a28e2171146241511",
                    city,
                    "8",
                    "no",
                    "ru"
                )
            } else if (lat != null && lon != null) {
                val q = "$lat,$lon"
                Log.d(
                    "CHECK",
                    "lat = $lat, lon = $lon"
                )
                service.fetchWeather(
                    "e098f2674efa47a28e2171146241511",
                    q,
                    "8",
                    "no",
                    "ru"
                )
            } else {
                Log.d(
                    "CHECK",
                    "Что то не так с ответом от сервера место: FetchForecastFor7DaysUseCase.invoke.response"
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
                            temp_c = body.current.temp_c,
                            temp_f = body.current.temp_f,
                            feelslike_c = body.current.feelslike_c,
                            feelslike_f = body.current.feelslike_f,
                            wind_mph = body.current.wind_mph,
                            wind_kph = body.current.wind_kph,
                            wind_degree = body.current.wind_degree,
                            wind_dir = body.current.wind_dir,
                            humidity = body.current.humidity,
                            gust_mph = body.current.gust_mph,
                            gust_kph = body.current.gust_kph,
                            uv = body.current.uv,
                            pressure_mb = body.current.pressure_mb,
                            pressure_in = body.current.pressure_in
                        ),

                        forecast = Forecast(
                            forecastday = body.forecast.forecastday.map { forecastDay ->
                                ForecastDay(
                                    date = forecastDay.date,
                                    day = Day(
                                        condition = WeatherCondition(
                                            text = forecastDay.day.condition.text,
                                            icon = forecastDay.day.condition.icon
                                        ),
                                        maxtemp_c = forecastDay.day.maxtemp_c,
                                        maxtemp_f = forecastDay.day.maxtemp_f,
                                        mintemp_c = forecastDay.day.mintemp_c,
                                        mintemp_f = forecastDay.day.mintemp_f,
                                        maxwind_mph = forecastDay.day.maxwind_mph,
                                        maxwind_kph = forecastDay.day.maxwind_kph,
                                        totalprecip_mm = forecastDay.day.totalprecip_mm,
                                        totalprecip_in = forecastDay.day.totalprecip_in,
                                        avghumidity = forecastDay.day.avghumidity
                                    ),
                                    hour = forecastDay.hour.map { hourResponse ->
                                        Hour(
                                            condition = WeatherCondition(
                                                text = hourResponse.condition.text,
                                                icon = hourResponse.condition.icon
                                            ),
                                            time = hourResponse.time,
                                            temp_c = hourResponse.temp_c,
                                            temp_f = hourResponse.temp_f,
                                            vis_km = hourResponse.vis_km,
                                            vis_miles = hourResponse.vis_miles
                                        )
                                    }
                                )
                            }
                        )
                    )
                    Log.d("CHECK", "data - $data")
                    database.dao().insertWeatherResponse(data)
                    Result.Success(data)
                } else {
                    Log.d(
                        "CHECK",
                        "Ответ от сервера пустой"
                    )
                    Result.Error(WeatherError.DataNotFound)
                }
            } else {
                Log.d("CHECK", "response errorBody - ${response.errorBody()}")
                Log.d(
                    "CHECK",
                    "Что то не так с ответом от сервера место: FetchForecastFor7DaysUseCase.invoke.!isSuccesful.NETWORK"
                )
                Result.Error(WeatherError.NetworkError)
            }
        } catch (e: Exception) {
            Log.d(
                "CHECK",
                "Что то не так с ответом от сервера место: FetchForecastFor7DaysUseCase.invoke.!isSuccesful.Excpection - ${e.stackTrace}\n${e.message}\n"
            )
            Result.Error(WeatherError.UnknownError(e.message ?: "Неизвестная ошибка"))
        }
    }
}