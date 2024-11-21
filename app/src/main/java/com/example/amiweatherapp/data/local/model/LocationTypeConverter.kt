package com.example.amiweatherapp.data.local.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LocationTypeConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromLocation(location: Location?): String? {
        return location?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toLocation(locationString: String?): Location? {
        return locationString?.let {
            val type = object : TypeToken<Location>() {}.type
            gson.fromJson(it, type)
        }
    }
}