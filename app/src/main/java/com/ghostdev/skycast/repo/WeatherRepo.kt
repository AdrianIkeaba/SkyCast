package com.ghostdev.skycast.repo

import com.ghostdev.skycast.datasource.WeatherDataSource
import com.ghostdev.skycast.model.AstronomyData
import com.ghostdev.skycast.model.CurrentWeather
import com.ghostdev.skycast.model.HourlyDataList
import com.ghostdev.skycast.model.WeatherForecast
import retrofit2.Response


class WeatherRepo(private val weatherDataSource: WeatherDataSource) {
    suspend fun getCurrentLocationWeather(): Response<CurrentWeather> {
        return weatherDataSource.getCurrentLocationWeather()
    }

    suspend fun getWeatherForecast(): Response<WeatherForecast> {
        return weatherDataSource.getWeatherForecast()
    }

    suspend fun getAstronomyData(): Response<AstronomyData> {
        return weatherDataSource.getAstronomyData()
    }

    suspend fun getHourlyData(): List<HourlyDataList> {
        return weatherDataSource.getHourlyData()
    }
}
