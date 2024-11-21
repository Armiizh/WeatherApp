package com.example.amiweatherapp.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "forecast_for_7_days")
data class ForecastFor7DaysResponse(
    @PrimaryKey
    val id: Int = 0,
    val location: Location,
    val current: CurrentWeather,
    val forecast: Forecast
)

data class Forecast(
    val forecastday: List<ForecastDay>
)

data class Location(
    //Город
    val name: String,
    //Область
    val region: String,
    //Страна
    val country: String,
    val lat: Double,
    val lon: Double
)

data class CurrentWeather(

    val condition: WeatherCondition,
    //Температура
    @SerializedName("temp_c") val temperatureC: Double,
    @SerializedName("temp_f") val temperatureF: Double,
    //Темп ощущается
    @SerializedName("feelslike_c") val feelsLikeC: Double,
    @SerializedName("feelslike_f") val feelsLikeF: Double,
    //Скорость ветра
    @SerializedName("wind_mph") val windMph: Double,
    @SerializedName("wind_kph") val windKph: Double,
    @SerializedName("wind_dir") val windDirection: String,
    //Влажность
    val humidity: Int,
    //Порывы ветра
    @SerializedName("gust_mph") val gustMph: Double,
    @SerializedName("gust_kph") val gustKph: Double,
)

data class WeatherCondition(
    //Состояние
    val text: String,
    //Ссылка на икноку
    val icon: String
)

data class ForecastDay(
    val date: String,        // Дата прогноза
    val date_epoch: Long,     // Прогнозируемая дата по времени Unix
    val day: Day,            // Элемент дня
    val hour: List<Hour>     // Элемент час
)

data class Day(
    val maxtemp_c: Double,       // Максимальная температура в градусах Цельсия
    val maxtemp_f: Double,       // Максимальная температура в градусах Фаренгейта
    val mintemp_c: Double,       // Минимальная температура в градусах Цельсия
    val mintemp_f: Double,       // Минимальная температура в градусах Фаренгейта

    val maxwind_mph: Double,     // Максимальная скорость ветра в милях в час
    val maxwind_kph: Double,     // Максимальная скорость ветра в километрах в час

    val totalprecip_mm: Double,  // Общее количество осадков в миллиметрах
    val totalprecip_in: Double,  // Общее количество осадков в дюймах

    val avghumidity: Int,        // Средняя влажность в процентах

    val condition: WeatherCondition, // Условия погоды

    val daily_will_it_rain: Int, // 1 = Да, 0 = Нет (Будет ли дождь)
    val daily_will_it_snow: Int, // 1 = Да, 0 = Нет (Пойдет снег)

    val daily_chance_of_rain: Int, // Вероятность дождя в процентах
    val daily_chance_of_snow: Int  // Вероятность выпадения снега в процентах
)

data class Hour(
    val time_epoch: Int,     // Время как эпоха
    val time: String,        // Дата и время
    val temp_c: Double,      // Температура в градусах Цельсия
    val temp_f: Double,      // Температура в градусах Фаренгейта
    val condition: WeatherCondition, // Условия погоды
    val wind_mph: Double,    // Максимальная скорость ветра в милях в час
    val wind_kph: Double,    // Максимальная скорость ветра в километрах в час
    val wind_dir: String,    // Направление ветра по 16-точечному компасу
    val pressure_mb: Double,  // Давление в миллибарах

    val precip_mm: Double,    // Количество осадков в миллиметрах
    val snow_cm: Double,       // Количество выпавшего снега в сантиметрах
    val humidity: Int,         // Влажность в процентах

    val feelslike_c: Double,   // Ощущается как температура в градусах Цельсия
    val feelslike_f: Double,   // Ощущается как температура по Фаренгейту

    val dewpoint_c: Double,    // Точка росы по Цельсию
    val dewpoint_f: Double,    // Точка росы в градусах Фаренгейта

    val will_it_rain: Int,     // 1 = Да, 0 = Нет (Будет ли дождь)
    val will_it_snow: Int,     // 1 = Да, 0 = Нет (Пойдет снег)

    val chance_of_rain: Int,   // Вероятность дождя в процентах
    val chance_of_snow: Int,   // Вероятность выпадения снега в процентах

    val gust_mph: Double,      // Порыв ветра в милях в час
    val gust_kph: Double,      // Порыв ветра в километрах в час
)