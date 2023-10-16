package com.ghostdev.skycast.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ghostdev.skycast.repo.WeatherRepo
import javax.inject.Inject

class WeatherViewModelFactory @Inject constructor(private val weatherRepo: WeatherRepo) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            return WeatherViewModel(weatherRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
