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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.quince.extensiones.decodeUnicodeCompletely
import com.example.quince.extensiones.decodeTildesAVersiAhora
import com.example.quince.mapa.paresProvCod
import com.example.quince.model.Provincias
import com.example.quince.navcontroller.Rutas
import com.example.quince.retrofit.ProvinciaViewModel
import com.example.quince.room.database.AppDatabase
import com.example.quince.room.database.AppDatabase.Companion.getDatabase
import com.example.quince.room.dataclasses.Recomendacion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue

//Aquí sí se usa viewmodel y debería usar también la base de datos interna.

@Composable
fun Segunda(
    navController: NavController,
    viewModel: ProvinciaViewModel = viewModel(), //El viewmodel hará que se carguen las provincias y decide cómo se van a mostrar.
    provincia: String) {

    val provinciaData: Provincias? by viewModel.provincias.observeAsState(initial = null) //Esto es para que se carguen las provincias.
    val db = getDatabase(navController.context)

    var recomendacion by remember { mutableStateOf<String?>(null) }


    LaunchedEffect(provincia) { //LaunchedEffect se ejecuta cuando cambia el valor de provincia y se usa en general para efectos secundarios.
        viewModel.cargarProvincias(provincia)
        // Ejecuta la consulta en un hilo en segundo plano.
        withContext(Dispatchers.IO) { //withContext se usa para cambiar el hilo de ejecución. El Dispathchers.IO es para operaciones de E/S.
            val comentario = db.daoRecomendacion().getRandomCommentInRange()
            // Después de obtener los datos, actualiza el estado en el hilo principal
            withContext(Dispatchers.Main) { //Aquí se cambia el hilo de ejecución a la principal (UI).
                recomendacion = comentario?.consejos?.decodeUnicodeCompletely()
            }
        }
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
                    // Mostrar la recomendación si está disponible
                    recomendacion?.let { recomendacionText ->
                        Text("Probando la recomendación de ${it.provincia.NOMBRE_PROVINCIA} que es: ${recomendacionText}")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
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
