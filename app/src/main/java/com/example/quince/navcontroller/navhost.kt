package com.example.quince.navcontroller

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.quince.pantallas.Primera
import com.example.quince.pantallas.Segunda


@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Rutas.PrimeraPantalla.ruta) {
        composable(route = Rutas.PrimeraPantalla.ruta) {
            Primera(navController = navController)
        }
        composable(route = "segundaPantalla/{provincia}") {
            Segunda(navController = navController, provincia = it.arguments?.getString("provincia"))
        }
    }}
//En el NavHost indico las pantallas que habrá