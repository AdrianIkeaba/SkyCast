package com.ghostdev.skycast.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.ghostdev.skycast.repo.WeatherRepo

class WeatherViewModel(private val weatherRepo: WeatherRepo): ViewModel() {

    fun getCurrentLocationWeather() = liveData {
        val weatherList = weatherRepo.getCurrentLocationWeather()
        emit(weatherList)
    }

    fun getWeatherForecast() = liveData {
        val weatherForecastList = weatherRepo.getWeatherForecast()
        emit(weatherForecastList)
    }

    fun getAstronomyData() = liveData {
        val weatherAstroList = weatherRepo.getAstronomyData()
        emit(weatherAstroList)
    }

    fun getHourlyData() = liveData {
        val weatherHourlyList = weatherRepo.getHourlyData()
        emit(weatherHourlyList)
    }
}