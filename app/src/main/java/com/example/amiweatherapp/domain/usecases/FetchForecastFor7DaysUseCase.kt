package com.example.amiweatherapp.domain.usecases

import android.util.Log
import com.example.amiweatherapp.data.local.ForecastFor7DaysDatabase
import com.example.amiweatherapp.data.local.model.CurrentWeather
import com.example.amiweatherapp.data.local.model.Day
import com.example.amiweatherapp.data.local.model.Forecast
import com.example.amiweatherapp.data.local.model.ForecastDay
import com.example.amiweatherapp.data.local.model.ForecastFor7DaysResponse
import com.example.amiweatherapp.data.local.model.Hour
import com.example.amiweatherapp.data.local.model.Location
import com.example.amiweatherapp.data.local.model.WeatherCondition
import com.example.amiweatherapp.data.service.Service
import com.example.amiweatherapp.data.utils.Result
import com.example.amiweatherapp.data.utils.WeatherError
import javax.inject.Inject

class FetchForecastFor7DaysUseCase @Inject constructor(
    private val service: Service,
    private val database: ForecastFor7DaysDatabase
) {
    suspend fun invoke(
        city: String? = null,
        lat: String? = null,
        lon: String? = null
    ): Result<ForecastFor7DaysResponse> {
        return try {
            val response = if (city != null) {
                Log.d(
                    "CHECK",
                    "city = $city"
                )
                service.forecast7Days(
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
                service.forecast7Days(
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
                    val data = ForecastFor7DaysResponse(
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
                        ),
                        forecast = Forecast(
                            forecastday = body.forecast.forecastday.map { forecastDay ->
                                ForecastDay(
                                    date = forecastDay.date,
                                    date_epoch = forecastDay.date_epoch,
                                    day = Day(
                                        maxtemp_c = forecastDay.day.maxtemp_c,
                                        maxtemp_f = forecastDay.day.maxtemp_f,
                                        mintemp_c = forecastDay.day.mintemp_c,
                                        mintemp_f = forecastDay.day.mintemp_f,
                                        maxwind_mph = forecastDay.day.maxwind_mph,
                                        maxwind_kph = forecastDay.day.maxwind_kph,
                                        totalprecip_mm = forecastDay.day.totalprecip_mm,
                                        totalprecip_in = forecastDay.day.totalprecip_in,
                                        avghumidity = forecastDay.day.avghumidity,
                                        condition = WeatherCondition(
                                            text = forecastDay.day.condition.text,
                                            icon = forecastDay.day.condition.icon
                                        ),
                                        daily_will_it_rain = forecastDay.day.daily_will_it_rain,
                                        daily_will_it_snow = forecastDay.day.daily_will_it_snow,
                                        daily_chance_of_rain = forecastDay.day.daily_chance_of_rain,
                                        daily_chance_of_snow = forecastDay.day.daily_chance_of_snow
                                    ),
                                    hour = forecastDay.hour.map { hourResponse ->
                                        Hour(
                                            time_epoch = hourResponse.time_epoch,
                                            time = hourResponse.time,
                                            temp_c = hourResponse.temp_c,
                                            temp_f = hourResponse.temp_f,
                                            condition = WeatherCondition(
                                                text = hourResponse.condition.text,
                                                icon = hourResponse.condition.icon
                                            ),
                                            wind_mph = hourResponse.wind_mph,
                                            wind_kph = hourResponse.wind_kph,
                                            wind_dir = hourResponse.wind_dir,
                                            pressure_mb = hourResponse.pressure_mb,
                                            precip_mm = hourResponse.precip_mm,
                                            snow_cm = hourResponse.snow_cm,
                                            humidity = hourResponse.humidity,
                                            feelslike_c = hourResponse.feelslike_c,
                                            feelslike_f = hourResponse.feelslike_f,
                                            dewpoint_c = hourResponse.dewpoint_c,
                                            dewpoint_f = hourResponse.dewpoint_f,
                                            will_it_rain = hourResponse.will_it_rain,
                                            will_it_snow = hourResponse.will_it_snow,
                                            chance_of_rain = hourResponse.chance_of_rain,
                                            chance_of_snow = hourResponse.chance_of_snow,
                                            gust_mph = hourResponse.gust_mph,
                                            gust_kph = hourResponse.gust_kph
                                        )
                                    }
                                )
                            }
                        )
                    )
                    Log.d("CHECK", "data - $data")
                    database.forecastFor7DaysDao().insertWeatherResponse(data)
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