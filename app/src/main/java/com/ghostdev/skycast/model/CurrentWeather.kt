package com.ghostdev.skycast.model

import com.google.gson.annotations.SerializedName

data class CurrentWeather(
    val location: Location,

    val current: Current

)

data class Location(

    @SerializedName("name")
    val name: String,

    @SerializedName("region")
    val region: String,

    @SerializedName("localtime")
    val localTime: String
)

data class Current(

    @SerializedName("temp_c")
    val tempCelsius: Double,

    @SerializedName("is_day")
    val isDay: Int,

    val condition: Condition,

    @SerializedName("wind_kph")
    val windKph: Double,

    @SerializedName("wind_degree")
    val windDegree: Int,

    @SerializedName("wind_dir")
    val windDirection: String,

    @SerializedName("humidity")
    val humidity: Int,

    @SerializedName("feelslike_c")
    val feelsLikeCelsius: Double,

    @SerializedName("uv")
    val uv: Double
)

data class Condition(

    @SerializedName("text")
    val weatherCondition: String
)