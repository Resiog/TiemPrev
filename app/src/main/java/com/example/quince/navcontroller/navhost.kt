package com.example.quince.navcontroller

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.quince.pantallas.Historial
import com.example.quince.pantallas.DetalleHistorial
import com.example.quince.pantallas.Primera
import com.example.quince.pantallas.Principal
import com.example.quince.pantallas.Segunda

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Rutas.Principal.ruta
    ) {
        //Defino las rutas de la app
        composable(route = Rutas.Principal.ruta) {
            Principal(navController = navController)
        }
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
        composable(route = Rutas.Historial.ruta) {
            Historial(navController = navController)
        }
        composable(
            route = "${Rutas.DetalleHistorial.ruta}/{provinciaId}",
            arguments = listOf(navArgument("provinciaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val provinciaId = backStackEntry.arguments?.getInt("provinciaId") ?: -1
            DetalleHistorial(navController = navController, provinciaId = provinciaId)
        }
    }}
//En el NavHost indico las pantallas que habrá. Es un contenedor de pantallas y se usa para navegar entre ellas.