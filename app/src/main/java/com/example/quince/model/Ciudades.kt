package com.example.quince.model

data class Ciudades(
    val id: String? = null,
//    val idProvince: WeatherInfo? = null,
//    val name: String,
//    val nameProvince: WeatherInfo? = null,
    val stateSky: StateInfo? = null,
    val temperatures: TempInfo? = null
)