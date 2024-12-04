package com.example.quince.model

data class Ciudades(
    val id: String? = null,
    //Comento las siguientes porque no voy a hacer uso de ellas
//    val idProvince: WeatherInfo? = null,
//    val name: String,
//    val nameProvince: WeatherInfo? = null,
    val stateSky: StateInfo? = null,
    val temperatures: TempInfo? = null
)

//Uso solo las propiedades que necesito