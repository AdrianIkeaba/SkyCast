package com.ghostdev.skycast.model

import com.google.gson.annotations.SerializedName

data class WeatherForecast(

    @SerializedName("forecast")
    val forecast: Forecast
)


data class Forecast(

    @SerializedName("forecastday")
    val forecastDay: List<ForecastDay>
)

data class ForecastDay(

    val date: String,

    val day: Day
)

data class Day(

    val condition: Condition,

    @SerializedName("temp_c")
    val tempCelsius: Double
)
