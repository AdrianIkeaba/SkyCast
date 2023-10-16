package com.ghostdev.skycast.model

import com.google.gson.annotations.SerializedName

data class AstronomyData(

    val location: Location,

    val astronomy: Astronomy
)

data class Astronomy(

    val astro: Astro
)

data class Astro(
    val sunrise: String,
    val sunset: String
)
