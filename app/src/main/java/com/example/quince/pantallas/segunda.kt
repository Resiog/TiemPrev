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
    provincia: String) {

    val provinciaData: Provincias? by viewModel.provincias.observeAsState(initial = null) //Esto es para que se carguen las provincias.
    val db = getDatabase(navController.context)

    var recomendacion by remember { mutableStateOf<String?>(null) }
    //Pruebo a ver si funciona
    var consejo by remember { mutableStateOf<String?>(null) }

    //Son dos LaunchedEffect:
    // -Uno para cuando cambie provincia, o sea, el String que entra y así actualice UI
    // -Otro para cuando cambie provinciaData, o sea, cuando se carguen las provincias y pueda así actualizar la BD

    LaunchedEffect(provincia) { //LaunchedEffect se ejecuta cuando cambia el valor de provincia.
        withContext(Dispatchers.IO) {
            viewModel.cargarProvincias(provincia)
        }
    }

    LaunchedEffect(provinciaData) {
        val nombreProvincia = provinciaData?.provincia?.NOMBRE_PROVINCIA
        if (!nombreProvincia.isNullOrEmpty()) {
            withContext(Dispatchers.IO) {
                //Lo de antes del comentario que tenía para verificar que funcionaba era.
                val comentario = db.daoRecomendacion().getRandomCommentInRange()
                recomendacion = comentario?.consejos?.decodeUnicodeCompletely()
                // Creo un objeto Provincia para insertar
                val provinciaAVerSiAhora = Provincia(name = nombreProvincia)

                // Inserto la provincia en la base de datos
                try {
                    db.daoProvincia().insertProvincia(provinciaAVerSiAhora)
                    Log.d("ProvinciaInsercion", "Provincia insertada correctamente.")
                } catch (e: Exception) {
                    Log.e("ProvinciaInsercion", "Error al insertar la provincia: ${e.message}")
                }

                //Meto aquí lo segundo que es el segundo insert
                val nombre = provinciaData?.provincia?.NOMBRE_PROVINCIA
                val nombreId = db.daoProvincia().getIdByName(nombre.toString())//Implementar aquí que el Id (como es FK) sea igual al ID de la tabla provincias que ponga
                val descripcion = provinciaData?.ciudades?.firstOrNull()?.stateSky?.description

                val maxTemp = provinciaData?.ciudades?.firstOrNull()?.temperatures?.max
                val minTemp = provinciaData?.ciudades?.firstOrNull()?.temperatures?.min

                val recomendacionProvincia : Recomendacion?
                val recomendacionId: Int?

                if (descripcion?.contains("Nuboso", ignoreCase = true) == true) {
                    // Si la descripción contiene "Nuboso", selecciona aleatoriamente entre los ID 3 y 5 y así con los demás sucesivamente
                    recomendacionProvincia = db.daoRecomendacion().getRandomCommentInRangeRange(1, 3)
                } else if (descripcion?.contains("Cubierto", ignoreCase = true) == true) {
                    recomendacionProvincia = db.daoRecomendacion().getRandomCommentInRangeRange(4, 6)
                } else if (descripcion?.contains("Despejado", ignoreCase = true) == true) {
                    recomendacionProvincia = db.daoRecomendacion().getRandomCommentInRangeRange(7, 9)
                } else if (descripcion?.contains("Nubes", ignoreCase = true) == true) {
                    recomendacionProvincia = db.daoRecomendacion().getRandomCommentInRangeRange(10, 12)
                } else if (descripcion?.contains("Lluvia", ignoreCase = true) == true) {
                    recomendacionProvincia = db.daoRecomendacion().getRandomCommentInRangeRange(13, 15)
                } else if (descripcion?.contains("Soleado", ignoreCase = true) == true) {
                    recomendacionProvincia = db.daoRecomendacion().getRandomCommentInRangeRange(16, 18)
                } else if (descripcion?.contains("Calor", ignoreCase = true) == true) {
                    recomendacionProvincia = db.daoRecomendacion().getRandomCommentInRangeRange(19, 21)
                } else {
                    // Si no se encuentra ninguna coincidencia, se obtiene un consejo aleatorio de todos los ID
                    recomendacionProvincia = db.daoRecomendacion().getRandomCommentInRangeRange(22, 29)
                }

                consejo = recomendacionProvincia?.consejos ?: "Sin recomendación"
                recomendacionId = recomendacionProvincia?.id // Obtener el ID de la recomendación

                // Creo un objeto Tiempo para insertar
                val tiempoAVer = Tiempo(
                    provincia = nombre?: "Desconocida",
                    provinciaId = nombreId?: -1,
                    descripcion = descripcion!!,
                    maxTemp = maxTemp!!.toDoubleOrNull() ?: 0.0,
                    minTemp = minTemp!!.toDoubleOrNull() ?: 0.0,
                    Recomendado = consejo!!,
                    consejoId = recomendacionId!!)

                // Inserto en la tabla tiempo todo
                try {
                    db.daoTiempo().insertTiempo(tiempoAVer)
                    Log.d("Tiempo inserción", "Provincia insertada correctamente.")
                } catch (e: Exception) {
                    Log.e("Tiempo inserción", "Error al insertar la provincia: ${e.message}")
                }
            }
        } else {
            Log.d("ProvinciaInsercion", "El nombre de la provincia es nulo o vacío.")
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState(), enabled = true)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)

        ) {
            provinciaData?.let {
                Text(
                    text = it.provincia.NOMBRE_PROVINCIA.decodeUnicodeCompletely(),
                    style = androidx.compose.material3.MaterialTheme.typography.headlineMedium,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 5.dp),
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Text(
                    //Negrita
                    text = buildAnnotatedString {
                        withStyle(style = androidx.compose.ui.text.SpanStyle(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)) {
                            append("Comunidad Autónoma: ")
                        }
                        append(it.provincia.COMUNIDAD_CIUDAD_AUTONOMA.decodeUnicodeCompletely())
                    }
                )

                //Estado del cielo
                val provinciaCodigos = paresProvCod[it.provincia.NOMBRE_PROVINCIA.decodeUnicodeCompletely()]
                it.ciudades?.firstOrNull { ciudad ->
                    ciudad.id == provinciaCodigos  // Esto filtra la ciudad que coincida con el id de la provincia
                }?.let { ciudad ->
                    // Accede a la propiedad stateSky.description solo si se encuentra la ciudad
                    ciudad.stateSky?.description?.let { estadoCielo ->
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = androidx.compose.ui.text.SpanStyle(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)) {
                                    append("Estado del cielo hoy: ")
                                }
                                append(estadoCielo.decodeTildesAVersiAhora())
                            }
                        )
                    }
                }

                Column {
                    //Temperaturas
                    it.ciudades?.firstOrNull { ciudad ->
                        ciudad.id == provinciaCodigos  // Filtra la ciudad que coincida con el id de la provincia
                    }?.let { ciudad ->
                        // Accede a la propiedad TempInfo, max y min solo si se encuentra la ciudad
                        ciudad.temperatures?.max?.let { estadoCielo ->
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(style = androidx.compose.ui.text.SpanStyle(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)) {
                                        append("Máximas de:  ")
                                    }
                                    append("${estadoCielo.decodeTildesAVersiAhora()}º")
                                }
                            )
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                        ciudad.temperatures?.min?.let { estadoCielo ->
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(style = androidx.compose.ui.text.SpanStyle(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)) {
                                        append("Mínimas de:  ")
                                    }
                                    append("${estadoCielo.decodeTildesAVersiAhora()}º")
                                }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    // Muestra la recomendación si está disponible
                    consejo?.let { consejoText ->
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = androidx.compose.ui.text.SpanStyle(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)) {
                                    append("Recomendación:  ")
                                }
                                append("${consejoText}")
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    it.today?.p?.let { tiempo ->
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = androidx.compose.ui.text.SpanStyle(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)) {
                                    append("Descripción completa hoy:  ")
                                }
                                append("${tiempo.decodeTildesAVersiAhora()}")
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    it.tomorrow?.p?.let { tiempo ->
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = androidx.compose.ui.text.SpanStyle(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)) {
                                    append("Descripción completa mañana:  ")
                                }
                                append("${tiempo.decodeTildesAVersiAhora()}")
                            }
                        )
                    }
                }
            } ?: Text("Cargando...")

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
                Text("Volver")
            }
        }
    }
}
