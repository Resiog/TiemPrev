package com.example.quince.pantallas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.quince.navcontroller.Rutas


@Composable
fun Segunda(navController: NavController, provincia: String?) {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Provincia seleccionada: ${provincia ?: "No seleccionada"}")

            // Botón "Ok"
            Button(
                onClick = {
                    navController.navigate(Rutas.PrimeraPantalla.ruta)
                },
                modifier = Modifier
                    .padding(bottom = 16.dp)
            ) {
                Text("Ok")
            }
        }
    }
}

@Composable
fun SegundaA() {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Segunda pantalla")

            // Botón "Ok"
            Button(
                onClick = { },
                modifier = Modifier
                    .padding(bottom = 16.dp)
            ) {
                Text("Ok")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SegundaPreview() {
    SegundaA()
}