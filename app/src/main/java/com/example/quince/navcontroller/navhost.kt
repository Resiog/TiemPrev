package com.example.quince.navcontroller

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.quince.pantallas.Primera
import com.example.quince.pantallas.Segunda


@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Rutas.PrimeraPantalla.ruta) {
        composable(route = Rutas.PrimeraPantalla.ruta) {
            Primera(navController = navController)
        }
        composable(
            route = "segundaPantalla/{provincia}",
            arguments = listOf(navArgument("provincia") { type = NavType.StringType })
        ) { backStackEntry ->
            val provincia = backStackEntry.arguments?.getString("provincia") ?: ""
            Segunda(
                navController = navController,
                provincia = provincia
            )
        }
    }}
//En el NavHost indico las pantallas que habrá. Es un contenedor de pantallas y se usa para navegar entre ellas.