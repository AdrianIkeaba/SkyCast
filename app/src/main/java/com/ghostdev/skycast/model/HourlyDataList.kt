package com.ghostdev.skycast.model

import com.google.gson.annotations.SerializedName

data class HourlyDataList(
    val forecast: Forecast2
)

data class Forecast2(
    @SerializedName("forecastday")
    val forecastDay: ForecastDay2
)

data class ForecastDay2(
    @SerializedName("hour")
    val hour: Hour
)

data class Hour(
    @SerializedName("time")
    val time: String,

    @SerializedName("temp_c")
    val tempCelsius: Double,

    @SerializedName("condition")
    val condition: Condition
)