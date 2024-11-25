package com.example.quince.navcontroller

sealed class Rutas (val ruta : String){
    object PrimeraPantalla : Rutas("primera")
    object SegundaPantalla : Rutas("segunda")
}