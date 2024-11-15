package com.example.amiweatherapp.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "weather_resp")
data class WeatherResp(
    @PrimaryKey
    val id: Int,
    val location: Location,
    val current: CurrentWeather
)

data class Location(
    val name: String,
    val region: String,
    val country: String,
    val lat: Double,
    val lon: Double,
    @SerializedName("tz_id") val timeZoneId: String,
    @SerializedName("localtime_epoch") val localTimeEpoch: Long,
    @SerializedName("localtime") val localTime: String
)

data class CurrentWeather(
    @SerializedName("last_updated_epoch") val lastUpdatedEpoch: Long,
    @SerializedName("last_updated") val lastUpdated: String,
    @SerializedName("temp_c") val temperatureC: Double,
    @SerializedName("temp_f") val temperatureF: Double,
    @SerializedName("is_day") val isDay: Int,
    val condition: WeatherCondition,
    @SerializedName("wind_mph") val windMph: Double,
    @SerializedName("wind_kph") val windKph: Double,
    @SerializedName("wind_degree") val windDegree: Int,
    @SerializedName("wind_dir") val windDirection: String,
    @SerializedName("pressure_mb") val pressureMb: Double,
    @SerializedName("pressure_in") val pressureIn: Double,
    @SerializedName("precip_mm") val precipitationMm: Double,
    @SerializedName("precip_in") val precipitationIn: Double,
    val humidity: Int,
    val cloud: Int,
    @SerializedName("feelslike_c") val feelsLikeC: Double,
    @SerializedName("feelslike_f") val feelsLikeF: Double,
    @SerializedName("windchill_c") val windChillC: Double,
    @SerializedName("windchill_f") val windChillF: Double,
    @SerializedName("heatindex_c") val heatIndexC: Double,
    @SerializedName("heatindex_f") val heatIndexF: Double,
    @SerializedName("dewpoint_c") val dewPointC: Double,
    @SerializedName("dewpoint_f") val dewPointF: Double,
    @SerializedName("vis_km") val visibilityKm: Double,
    @SerializedName("vis_miles") val visibilityMiles: Double,
    val uv: Double,
    @SerializedName("gust_mph") val gustMph: Double,
    @SerializedName("gust_kph") val gustKph: Double
)

data class WeatherCondition(
    val text: String,
    val icon: String,
    val code: Int
)