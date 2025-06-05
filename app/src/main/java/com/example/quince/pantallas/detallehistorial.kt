package com.example.quince.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.quince.model.Provincias
import com.example.quince.room.historialviewmodel.DetalleHistorialViewModel
import com.google.gson.Gson

@Composable
fun DetalleHistorial(
    navController: NavController,
    provinciaId: Int,
    viewModel: DetalleHistorialViewModel = viewModel()
) {
    LaunchedEffect(provinciaId) {
        viewModel.cargarTiempoPorProvinciaId(provinciaId)
    }

    val tiempoState by viewModel.tiempo.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(gradientStartColor, gradientEndColor)
                )
            )
    ) {
        tiempoState?.let { tiempo ->
            val provincias = Gson().fromJson(tiempo.dataJson, Provincias::class.java)
            SuccessState(provincias, tiempo.Recomendado, navController)
        } ?: LoadingState()
    }
}
