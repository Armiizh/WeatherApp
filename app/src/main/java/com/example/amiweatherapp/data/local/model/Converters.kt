package com.example.amiweatherapp.data.local.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    // Конвертер для Coord
    @TypeConverter
    fun fromCoord(coord: Coord): String {
        return "${coord.lon},${coord.lat}"
    }

    @TypeConverter
    fun toCoord(coordString: String): Coord {
        val parts = coordString.split(",")
        return Coord(lon = parts[0].toDouble(), lat = parts[1].toDouble())
    }

    // Конвертер для списка Weather
    @TypeConverter
    fun fromWeatherList(weatherList: List<Weather>): String {
        return weatherList.joinToString(";") { "${it.id},${it.main},${it.description},${it.icon}" }
    }

    @TypeConverter
    fun toWeatherList(weatherString: String): List<Weather> {
        return weatherString.split(";").map {
            val parts = it.split(",")
            Weather(
                id = parts[0].toInt(),
                main = parts[1],
                description = parts[2],
                icon = parts[3]
            )
        }
    }

    // Конвертер для Main
    @TypeConverter
    fun fromMain(main: Main): String {
        return "${main.temp},${main.feels_like},${main.temp_min},${main.temp_max},${main.pressure},${main.humidity},${main.sea_level},${main.grnd_level}"
    }

    @TypeConverter
    fun toMain(mainString: String): Main {
        val parts = mainString.split(",")
        return Main(
            temp = parts[0].toDouble(),
            feels_like = parts[1].toDouble(),
            temp_min = parts[2].toDouble(),
            temp_max = parts[3].toDouble(),
            pressure = parts[4].toInt(),
            humidity = parts[5].toInt(),
            sea_level = parts[6].toInt(),
            grnd_level = parts[7].toInt()
        )
    }

    // Конвертер для Wind
    @TypeConverter
    fun fromWind(wind: Wind): String {
        return "${wind.speed},${wind.deg},${wind.gust}"
    }

    @TypeConverter
    fun toWind(windString: String): Wind {
        val parts = windString.split(",")
        return Wind(
            speed = parts[0].toDouble(),
            deg = parts[1].toInt(),
            gust = parts[2].toDouble()
        )
    }

    // Конвертер для Clouds
    @TypeConverter
    fun fromClouds(clouds: Clouds): Int {
        return clouds.all
    }

    @TypeConverter
    fun toClouds(all: Int): Clouds {
        return Clouds(all = all)
    }

    // Конвертер для Sys
    @TypeConverter
    fun fromSys(sys: Sys): String {
        return "${sys.type},${sys.id},${sys.country},${sys.sunrise},${sys.sunset}"
    }

    @TypeConverter
    fun toSys(sysString: String): Sys {
        val parts = sysString.split(",")
        return Sys(
            type = parts[0].toInt(),
            id = parts[1].toInt(),
            country = parts[2],
            sunrise = parts[3].toLong(),
            sunset = parts[4].toLong()
        )
    }
}