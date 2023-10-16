package com.ghostdev.skycast.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CurrentDateProvider {
    fun getCurrentDate(): String {
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val currentDate = LocalDate.now()
        return currentDate.format(dateFormatter)
    }
}