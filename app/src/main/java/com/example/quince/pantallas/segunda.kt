package com.example.quince.pantallas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.quince.extensiones.decodeUnicodeCompletely
import com.example.quince.extensiones.decodeTildesAVersiAhora
import com.example.quince.mapa.paresProvCod
import com.example.quince.model.Provincias
import com.example.quince.navcontroller.Rutas
import com.example.quince.retrofit.ProvinciaViewModel
import com.example.quince.room.database.AppDatabase.Companion.getDatabase
import com.example.quince.room.dataclasses.Recomendacion


@Composable
fun Segunda(
    navController: NavController,
    viewModel: ProvinciaViewModel = viewModel(),
    provincia: String) {

    val provinciaData: Provincias? by viewModel.provincias.observeAsState(initial = null)

    LaunchedEffect(provincia) {
        viewModel.cargarProvincias(provincia)
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            provinciaData?.let {
                Column {
                    Text("Provincia: ${it.provincia.NOMBRE_PROVINCIA.decodeUnicodeCompletely()}")
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Comunidad Autónoma: ${it.provincia.COMUNIDAD_CIUDAD_AUTONOMA.decodeUnicodeCompletely()}")
                    Spacer(modifier = Modifier.height(16.dp))

                    // Debo hacer un mapOf para que funcione el pasarle código de provincia y que me de estado del cielo
                    //Esto me saca el código de la provincia en concreto:
                    val provinciaCodigos = paresProvCod[it.provincia.NOMBRE_PROVINCIA.decodeUnicodeCompletely()]
                    it.ciudades?.firstOrNull { ciudad ->
                        ciudad.id == provinciaCodigos  // Filtra la ciudad que coincida con el id de la provincia
                    }?.let { ciudad ->
                        // Acceder a la propiedad stateSky.description solo si se encuentra la ciudad
                        ciudad.stateSky?.description?.let { estadoCielo ->
                            Text("Estado del cielo hoy: ${estadoCielo.decodeTildesAVersiAhora()}")
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    //Temperaturas
                    it.ciudades?.firstOrNull { ciudad ->
                        ciudad.id == provinciaCodigos  // Filtra la ciudad que coincida con el id de la provincia
                    }?.let { ciudad ->
                        // Acceder a la propiedad TempInfo, max y min solo si se encuentra la ciudad
                        ciudad.temperatures?.max?.let { estadoCielo ->
                            Text("Temperatura máxima prevista de ${estadoCielo.decodeTildesAVersiAhora()}º")
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        ciudad.temperatures?.min?.let { estadoCielo ->
                            Text("Temperatura mínima prevista de ${estadoCielo.decodeTildesAVersiAhora()}º")
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
//
                    //
                    it.today?.p?.let { tiempo ->
                        Text("Descripción completa del tiempo de hoy: ${tiempo.decodeTildesAVersiAhora()}")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    it.tomorrow?.p?.let { tiempo ->
                        Text("Descripción del tiempo previsto para mañana: ${tiempo.decodeTildesAVersiAhora()}")
                    }
                }
            } ?: Text("Cargando...")


            // Botón "Ok"
            Button(
                onClick = {
                    navController.navigate(Rutas.PrimeraPantalla.ruta){
                        popUpTo(Rutas.PrimeraPantalla.ruta) {
                            inclusive = true
                        }
                    }
                },
                modifier = Modifier
                    .padding(bottom = 16.dp)
            ) {
                Text("Ok")
            }
        }
    }
}

//Estructura inicial:
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