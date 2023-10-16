package com.ghostdev.skycast.datasource


import com.ghostdev.skycast.util.CurrentDateProvider
import com.ghostdev.skycast.api.WeatherService
import com.ghostdev.skycast.model.AstronomyData
import com.ghostdev.skycast.model.CurrentHourlyData
import com.ghostdev.skycast.model.CurrentWeather
import com.ghostdev.skycast.model.HourlyDataList
import com.ghostdev.skycast.model.WeatherForecast
import retrofit2.Response

class WeatherDataSourceImpl(
    private val weatherService: WeatherService,
    private val key: String,
    private val location: String
) : WeatherDataSource {

    private val date = CurrentDateProvider().getCurrentDate()


    override suspend fun getCurrentLocationWeather(): Response<CurrentWeather> {
        return weatherService.getCurrentLocationWeather(location, key)
    }

    override suspend fun getWeatherForecast(): Response<WeatherForecast> {
        return weatherService.getWeatherForecast(location, 7, 12, key)
    }

    override suspend fun getAstronomyData(): Response<AstronomyData> {
        return weatherService.getAstronomyData(location, date, key)
    }

    override suspend fun getHourlyData(): List<HourlyDataList> {
        return weatherService.getCurrentHourlyData(location, 1, 1, key)
    }
}

