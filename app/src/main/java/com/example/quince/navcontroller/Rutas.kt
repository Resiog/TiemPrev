package com.example.quince.navcontroller

sealed class Rutas (val ruta : String){
    object PrimeraPantalla : Rutas("primera")
    object SegundaPantalla : Rutas("segunda")
    object Principal: Rutas ("principal")
    object Historial: Rutas ("historial")
    object DetalleHistorial: Rutas("detalleHistorial")
}