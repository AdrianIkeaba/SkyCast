package com.ghostdev.skycast.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherServiceImpl(baseUrl: String) : WeatherService {

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val weatherService: WeatherService = retrofit.create(WeatherService::class.java)

    override suspend fun getCurrentLocationWeather(location: String, apiKey: String) =
        weatherService.getCurrentLocationWeather(location, apiKey)

    override suspend fun getWeatherForecast(location: String, days: Int, hour: Int, apiKey: String) =
        weatherService.getWeatherForecast(location, days, hour, apiKey)

    override suspend fun getAstronomyData(location: String, date: String, apiKey: String) =
        weatherService.getAstronomyData(location, date, apiKey)

    override suspend fun getCurrentHourlyData(location: String, days: Int, tp: Int, key: String) =
        weatherService.getCurrentHourlyData(location, days, tp, key)
}
