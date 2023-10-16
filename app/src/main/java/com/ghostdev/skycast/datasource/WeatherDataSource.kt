package com.ghostdev.skycast.datasource

import com.ghostdev.skycast.model.AstronomyData
import com.ghostdev.skycast.model.CurrentHourlyData
import com.ghostdev.skycast.model.CurrentWeather
import com.ghostdev.skycast.model.HourlyDataList
import com.ghostdev.skycast.model.WeatherForecast
import retrofit2.Response

interface WeatherDataSource {

    suspend fun getCurrentLocationWeather(): Response<CurrentWeather>

    suspend fun getWeatherForecast(): Response<WeatherForecast>

    suspend fun getAstronomyData(): Response<AstronomyData>

    suspend fun getHourlyData(): List<HourlyDataList>
}