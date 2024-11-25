package com.example.quince.model

data class Provincias(
    val provincia: ProvinciaDetalle,
    val title: String,
    val today: WeatherInfo? = null,
    val tomorrow: WeatherInfo? = null
)
