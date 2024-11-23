package com.example.amiweatherapp.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "weather_response")
data class WeatherResponse(

    @PrimaryKey val id: Int = 0,
    @SerializedName("location") val location: Location,      //Геолокация
    @SerializedName("current") val current: CurrentWeather,  //Текущая погода
    @SerializedName("forecast") val forecast: Forecast       //Прогноз
)

data class Location(

    @SerializedName("name") val name: String,        //Город
    @SerializedName("region") val region: String,    //Область
    @SerializedName("country") val country: String,  //Страна
    @SerializedName("lat") val lat: Double,          //Координаты долготы
    @SerializedName("lon") val lon: Double           //Координаты широты
)

data class CurrentWeather(

    val condition: WeatherCondition,  // Условия погоды

    @SerializedName("temp_c") val temp_c: Double,            //Температура в градусах Цельсия
    @SerializedName("temp_f") val temp_f: Double,            //Температура в градусах Фаренгейта
    @SerializedName("feelslike_c") val feelslike_c: Double,  //Ощущается как температура в градусах Цельсия
    @SerializedName("feelslike_f") val feelslike_f: Double,  //Ощущается как температура в градусах Фаренгейта
    @SerializedName("wind_mph") val wind_mph: Double,        //Скорость ветра в милях в час
    @SerializedName("wind_kph") val wind_kph: Double,        //Скорость ветра в километрах в час
    @SerializedName("wind_degree") val wind_degree: Int,     //Направление ветра в градусах
    @SerializedName("wind_dir") val wind_dir: String,        //Направление ветра по 16-точечному компасу
    @SerializedName("humidity") val humidity: Int,           //Влажность в процентах
    @SerializedName("gust_mph") val gust_mph: Double,        //Порыв ветра в милях в час
    @SerializedName("gust_kph") val gust_kph: Double,        //Порыв ветра в километрах в час
    @SerializedName("uv") val uv: Double,                    //УФ-индекс
    @SerializedName("pressure_mb") val pressure_mb: Double,  //Давление в миллибарах
    @SerializedName("pressure_in") val pressure_in: Double   //Давление в дюймах
)

data class Forecast(
    @SerializedName("forecastday") val forecastday: List<ForecastDay>  //Список из прогнозов погоды на день
)

data class ForecastDay(

    @SerializedName("date") val date: String,       // Дата прогноза
    @SerializedName("day") val day: Day,            // Элемент дня
    @SerializedName("hour") val hour: List<Hour>    // Элемент час
)

data class Day(

    val condition: WeatherCondition,  // Условия погоды

    @SerializedName("mintemp_c") val mintemp_c: Double,            // Минимальная температура в градусах Цельсия
    @SerializedName("mintemp_f") val mintemp_f: Double,            // Минимальная температура в градусах Фаренгейта
    @SerializedName("maxtemp_c") val maxtemp_c: Double,            // Максимальная температура в градусах Цельсия
    @SerializedName("maxtemp_f") val maxtemp_f: Double,            // Максимальная температура в градусах Фаренгейта
    @SerializedName("maxwind_mph") val maxwind_mph: Double,        // Максимальная скорость ветра в милях в час
    @SerializedName("maxwind_kph") val maxwind_kph: Double,        // Максимальная скорость ветра в километрах в час
    @SerializedName("totalprecip_mm") val totalprecip_mm: Double,  // Общее количество осадков в миллиметрах
    @SerializedName("totalprecip_in") val totalprecip_in: Double,  // Общее количество осадков в дюймах
    @SerializedName("avghumidity") val avghumidity: Int,           // Средняя влажность в процентах
)

data class Hour(

    val condition: WeatherCondition,  // Условия погоды

    @SerializedName("time") val time: String,            // Дата и время
    @SerializedName("temp_c") val temp_c: Double,        // Температура в градусах Цельсия
    @SerializedName("temp_f") val temp_f: Double,        // Температура в градусах Фаренгейта
    @SerializedName("vis_km") val vis_km: Double,        //Видимость в километрах
    @SerializedName("vis_miles") val vis_miles: Double,  //Видимость в милях
)

data class WeatherCondition(

    @SerializedName("text") val text: String, //Состояние
    @SerializedName("icon") val icon: String  //Ссылка на икноку
)
