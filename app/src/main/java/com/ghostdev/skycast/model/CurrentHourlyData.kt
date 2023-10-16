package com.ghostdev.skycast.model

import com.google.gson.annotations.SerializedName

data class CurrentHourlyData(
    @SerializedName("hour")
    val hour: List<HourlyDataList>
)