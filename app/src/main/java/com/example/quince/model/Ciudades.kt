package com.example.quince.model

data class Ciudades(
    val id: Map<String, String>? = null, // Corregido a Map
    val idProvince: String? = null,      // Añadido
    val name: String? = null,            // Añadido
    val nameProvince: String? = null,    // Añadido
    val stateSky: StateInfo? = null,
    val temperatures: TempInfo? = null
)

