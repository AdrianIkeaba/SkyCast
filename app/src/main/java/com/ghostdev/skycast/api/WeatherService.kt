package com.ghostdev.skycast.api

import com.ghostdev.skycast.model.AstronomyData
import com.ghostdev.skycast.model.CurrentHourlyData
import com.ghostdev.skycast.model.CurrentWeather
import com.ghostdev.skycast.model.HourlyDataList
import com.ghostdev.skycast.model.WeatherForecast
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("current.json")
    suspend fun getCurrentLocationWeather(@Query("q") location: String, @Query("key") apiKey: String): Response<CurrentWeather>

    @GET("forecast.json")
    suspend fun getWeatherForecast(@Query("q") location: String, @Query("days") days: Int, @Query("hour") hour: Int, @Query("key") apiKey: String): Response<WeatherForecast>

    @GET("astronomy.json")
    suspend fun getAstronomyData(@Query("q") location: String, @Query("dt") date: String, @Query("key") apiKey: String): Response<AstronomyData>

    @GET("forecast.json")
    suspend fun getCurrentHourlyData(@Query("q") location: String, @Query("days") days: Int, @Query("tp") tp: Int, @Query("key") key: String): List<HourlyDataList>
}