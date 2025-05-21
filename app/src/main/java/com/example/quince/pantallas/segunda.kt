package com.example.quince.pantallas

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.quince.extensiones.decodeUnicodeCompletely
import com.example.quince.extensiones.decodeTildesAVersiAhora
import com.example.quince.mapa.paresProvCod
import com.example.quince.model.Provincias
import com.example.quince.model.ResultWrapper // Import ResultWrapper
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
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import com.example.quince.room.dataclasses.Provincia
import com.example.quince.room.dataclasses.Tiempo

//Aquí sí se usa viewmodel y debería usar también la base de datos interna.

@Composable
fun Segunda(
    navController: NavController,
    viewModel: ProvinciaViewModel = viewModel(), //El viewmodel hace que se carguen las provincias y decide cómo se van a mostrar.
    provincia: String) { // provincia is CODPROV e.g. "01"

    val db = getDatabase(navController.context)
    var consejo by remember { mutableStateOf<String?>(null) }

    // LaunchedEffect for fetching data
    LaunchedEffect(provincia) {
        viewModel.cargarProvincias(provincia)
    }

    val provinciaResult: ResultWrapper<Provincias>? by viewModel.provincias.observeAsState()

    // LaunchedEffect for DB operations, should only run on Success
    LaunchedEffect(provinciaResult) {
        if (provinciaResult is ResultWrapper.Success) {
            val successData = (provinciaResult as ResultWrapper.Success<Provincias>).data
            val nombreProvincia = successData.provincia.NOMBRE_PROVINCIA
            if (!nombreProvincia.isNullOrEmpty()) {
                withContext(Dispatchers.IO) {
                    // Creo un objeto Provincia para insertar
                    val provinciaAVerSiAhora = Provincia(name = nombreProvincia.decodeUnicodeCompletely()) // Decode here for DB

                    // Inserto la provincia en la base de datos
                    try {
                        db.daoProvincia().insertProvincia(provinciaAVerSiAhora)
                        Log.d("ProvinciaInsercion", "Provincia insertada correctamente: ${provinciaAVerSiAhora.name}")
                    } catch (e: Exception) {
                        Log.e("ProvinciaInsercion", "Error al insertar la provincia: ${e.message}")
                    }

                    //Meto aquí lo segundo que es el segundo insert
                    val nombreId = db.daoProvincia().getIdByName(nombreProvincia.decodeUnicodeCompletely()) // Use decoded name

                    // Corrected city data access for DB
                    val provinciaNombreDecodedForDb = successData.provincia.NOMBRE_PROVINCIA.decodeUnicodeCompletely()
                    val ciudadIdParaCompararForDb = paresProvCod[provinciaNombreDecodedForDb]
                    val ciudadEncontradaForDb = successData.ciudades?.firstOrNull { ciudad ->
                        ciudad.id?.get("0") == ciudadIdParaCompararForDb
                    }

                    val descripcion = ciudadEncontradaForDb?.stateSky?.description
                    val maxTemp = ciudadEncontradaForDb?.temperatures?.max
                    val minTemp = ciudadEncontradaForDb?.temperatures?.min

                    val recomendacionProvincia : Recomendacion?
                    val recomendacionId: Int?

                    if (descripcion?.contains("Cubierto", ignoreCase = true) == true) {
                        recomendacionProvincia = db.daoRecomendacion().getRandomCommentInRangeRange(1, 10)
                    } else if (descripcion?.contains("Despejado", ignoreCase = true) == true) {
                        recomendacionProvincia = db.daoRecomendacion().getRandomCommentInRangeRange(11, 20)
                    } else if (descripcion?.contains("Nubes", ignoreCase = true) == true) {
                        recomendacionProvincia = db.daoRecomendacion().getRandomCommentInRangeRange(21, 30)
                    } else if (descripcion?.contains("Lluvia", ignoreCase = true) == true) {
                        recomendacionProvincia = db.daoRecomendacion().getRandomCommentInRangeRange(31, 40)
                    } else if (descripcion?.contains("Soleado", ignoreCase = true) == true) {
                        recomendacionProvincia = db.daoRecomendacion().getRandomCommentInRangeRange(41, 50)
                    } else if (descripcion?.contains("Calor", ignoreCase = true) == true) {
                        recomendacionProvincia = db.daoRecomendacion().getRandomCommentInRangeRange(51, 60)
                    } else if (descripcion?.contains("Nuboso", ignoreCase = true) == true) {
                        recomendacionProvincia = db.daoRecomendacion().getRandomCommentInRangeRange(61, 70)
                    } else {
                        recomendacionProvincia = db.daoRecomendacion().getRandomCommentInRangeRange(71, 80)
                    }

                    consejo = recomendacionProvincia?.consejos?.decodeUnicodeCompletely() ?: "Sin recomendación disponible."
                    recomendacionId = recomendacionProvincia?.id

                    // Creo un objeto Tiempo para insertar
                    val tiempoAVer = Tiempo(
                        provincia = nombreProvincia.decodeUnicodeCompletely(), // Use decoded name
                        provinciaId = nombreId ?: -1, // Fallback if ID not found
                        descripcion = descripcion ?: "No disponible",
                        maxTemp = maxTemp?.toDoubleOrNull() ?: 0.0,
                        minTemp = minTemp?.toDoubleOrNull() ?: 0.0,
                        Recomendado = consejo ?: "Sin recomendación",
                        consejoId = recomendacionId ?: -1 // Fallback if ID not found
                    )

                    // Inserto en la tabla tiempo todo
                    try {
                        db.daoTiempo().insertTiempo(tiempoAVer)
                        Log.d("Tiempo inserción", "Tiempo insertado correctamente para ${tiempoAVer.provincia}.")
                    } catch (e: Exception) {
                        Log.e("Tiempo inserción", "Error al insertar tiempo: ${e.message}")
                    }
                }
            } else {
                Log.d("DBOperations", "El nombre de la provincia es nulo o vacío, no se realizan operaciones de BD.")
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        when (val result = provinciaResult) {
            is ResultWrapper.Loading -> {
                CircularProgressIndicator()
                Text("Cargando datos...", style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center, modifier = Modifier.padding(top = 70.dp))
            }
            is ResultWrapper.Success -> {
                val successData = result.data
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState(), enabled = true)
                        .fillMaxHeight()
                        .padding(vertical = 16.dp), // Add some vertical padding
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = successData.provincia.NOMBRE_PROVINCIA.decodeUnicodeCompletely(),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 5.dp),
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = androidx.compose.ui.text.SpanStyle(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)) {
                                append("Comunidad Autónoma: ")
                            }
                            append(successData.provincia.COMUNIDAD_CIUDAD_AUTONOMA.decodeUnicodeCompletely())
                        }
                    )

                    // Corrected logic for city-specific details
                    val provinciaNombreDecoded = successData.provincia.NOMBRE_PROVINCIA.decodeUnicodeCompletely()
                    val ciudadIdParaComparar = paresProvCod[provinciaNombreDecoded]

                    val ciudadEncontrada = successData.ciudades?.firstOrNull { ciudad ->
                        ciudad.id?.get("0") == ciudadIdParaComparar
                    }

                    ciudadEncontrada?.let { cityData ->
                        cityData.stateSky?.description?.let { estadoCielo ->
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(style = androidx.compose.ui.text.SpanStyle(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)) {
                                        append("Estado del cielo hoy (${cityData.name?.decodeUnicodeCompletely() ?: "ciudad"}): ")
                                    }
                                    append(estadoCielo.decodeTildesAVersiAhora())
                                }
                            )
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                        cityData.temperatures?.max?.let { maxTemp ->
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(style = androidx.compose.ui.text.SpanStyle(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)) {
                                        append("Máximas de:  ")
                                    }
                                    append("${maxTemp.decodeTildesAVersiAhora()}º")
                                }
                            )
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                        cityData.temperatures?.min?.let { minTemp ->
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(style = androidx.compose.ui.text.SpanStyle(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)) {
                                        append("Mínimas de:  ")
                                    }
                                    append("${minTemp.decodeTildesAVersiAhora()}º")
                                }
                            )
                        }
                    } ?: run {
                        Text("Datos específicos de la ciudad capital no encontrados.")
                    }


                    Spacer(modifier = Modifier.height(8.dp))
                    // Muestra la recomendación si está disponible
                    consejo?.let { consejoText ->
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = androidx.compose.ui.text.SpanStyle(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)) {
                                    append("Recomendación:  ")
                                }
                                append(consejoText) // consejo is already decoded in LaunchedEffect
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    successData.today?.p?.let { tiempo ->
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = androidx.compose.ui.text.SpanStyle(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)) {
                                    append("Descripción completa hoy:  ")
                                }
                                append(tiempo.decodeTildesAVersiAhora())
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    successData.tomorrow?.p?.let { tiempo ->
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = androidx.compose.ui.text.SpanStyle(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)) {
                                    append("Descripción completa mañana:  ")
                                }
                                append(tiempo.decodeTildesAVersiAhora())
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp)) // Added spacer before button
                    Button(
                        onClick = { navController.popBackStack() }, // Simpler navigation
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        Text("Volver")
                    }
                }
            }
            is ResultWrapper.Error -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize().padding(16.dp)
                ) {
                    Text("Error", style = MaterialTheme.typography.headlineSmall, color = Color.Red)
                    Text(result.message ?: "Ocurrió un error desconocido.", textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyLarge)
                    result.exception.localizedMessage?.let {
                        Text("Detalles: $it", style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center, modifier = Modifier.padding(top = 8.dp))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { navController.popBackStack() }) {
                        Text("Volver")
                    }
                }
            }
            null -> {
                Text("Iniciando...", style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center)
            }
        }
    }
}
