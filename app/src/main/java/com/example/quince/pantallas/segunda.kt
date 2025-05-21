package com.example.quince.pantallas

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Dehaze
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Grain
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Thunderstorm
import androidx.compose.material.icons.outlined.WbCloudy
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.example.quince.room.dataclasses.Provincia
import com.example.quince.room.dataclasses.Tiempo

//Aquí sí se usa viewmodel y debería usar también la base de datos interna.

//Gemini
// Simulación por si no están en el mismo archivo/scope.
fun String.decodeUnicodeCompletely(): String = decodeUnicodeCompletely() // Reemplaza con tu implementación real
fun String.decodeTildesAVersiAhora(): String = decodeTildesAVersiAhora() // Reemplaza con tu implementación real


 val cardBackgroundColor = Color.White.copy(alpha = 0.8f) // Tarjetas semi-transparentes
 val cardContentColor = Color(0xFF333333)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Segunda(
    navController: NavController,
    viewModel: ProvinciaViewModel = viewModel(),
    provincia: String // CODPROV
) {
    val db = getDatabase(navController.context)
    var consejo by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(provincia) {
        viewModel.cargarProvincias(provincia)
    }

    val provinciaResult: ResultWrapper<Provincias>? by viewModel.provincias.observeAsState()

    LaunchedEffect(provinciaResult) {
        if (provinciaResult is ResultWrapper.Success) {
            val successData = (provinciaResult as ResultWrapper.Success<Provincias>).data
            val nombreProvincia = successData.provincia.NOMBRE_PROVINCIA
            if (!nombreProvincia.isNullOrEmpty()) {
                withContext(Dispatchers.IO) {
                    // --- TU LÓGICA DE BASE DE DATOS (se mantiene como la proporcionaste) ---
                    // Creo un objeto Provincia para insertar
                    val provinciaAVerSiAhora = Provincia(name = nombreProvincia.decodeUnicodeCompletely()) // Decode here for DB
                    try {
                        db.daoProvincia().insertProvincia(provinciaAVerSiAhora)
                    } catch (e: Exception) { /* ... */ }

                    val nombreId = db.daoProvincia().getIdByName(nombreProvincia.decodeUnicodeCompletely())
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
                    // ... (lógica de obtención de recomendación)
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
                    val tiempoAVer = Tiempo(
                        provincia = nombreProvincia.decodeUnicodeCompletely(),
                        provinciaId = nombreId ?: -1,
                        descripcion = descripcion ?: "No disponible",
                        maxTemp = maxTemp?.toDoubleOrNull() ?: 0.0,
                        minTemp = minTemp?.toDoubleOrNull() ?: 0.0,
                        Recomendado = consejo ?: "Sin recomendación",
                        consejoId = recomendacionId ?: -1
                    )
                    try {
                        db.daoTiempo().insertTiempo(tiempoAVer)
                    } catch (e: Exception) { /* ... */ }
                    // --- FIN DE TU LÓGICA DE BASE DE DATOS ---
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(gradientStartColor, gradientEndColor)
                )
            )
    ) {
        when (val result = provinciaResult) {
            is ResultWrapper.Loading -> LoadingState()
            is ResultWrapper.Success -> SuccessState(result.data, consejo, navController)
            is ResultWrapper.Error -> ErrorState(result.message ?: "Error desconocido.", navController)
            null -> InitializingState() // Podrías tener un estado inicial si es necesario
        }
    }
}

@Composable
fun LoadingState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(60.dp),
            color = titleTextColor, // Usa un color de tu tema
            strokeWidth = 5.dp
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            "Cargando datos del tiempo...",
            fontSize = 18.sp,
            color = titleTextColor,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ErrorState(errorMessage: String, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Filled.ErrorOutline,
            contentDescription = "Error",
            tint = Color.Red.copy(alpha = 0.8f),
            modifier = Modifier.size(70.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "¡Ups! Algo salió mal",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = titleTextColor,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            errorMessage,
            fontSize = 16.sp,
            color = titleTextColor.copy(alpha = 0.8f),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        StyledAppButton( // Reutiliza tu botón estilizado
            text = "Volver",
            icon = Icons.Filled.ArrowBackIosNew,
            onClick = { navController.popBackStack() }
        )
    }
}

@Composable
fun InitializingState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Iniciando...", fontSize = 18.sp, color = titleTextColor)
    }
}


@Composable
fun SuccessState(
    data: Provincias,
    consejoText: String?,
    navController: NavController
) {
    val scrollState = rememberScrollState()
    val provinciaNombreDecoded = data.provincia.NOMBRE_PROVINCIA.decodeUnicodeCompletely()
    val ciudadIdParaComparar = paresProvCod[provinciaNombreDecoded]
    val ciudadEncontrada = data.ciudades?.firstOrNull { ciudad ->
        ciudad.id?.get("0") == ciudadIdParaComparar
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título de la provincia
        ScreenTitle(
            text = provinciaNombreDecoded,
            icon = Icons.Filled.LocationCity, // O un icono más específico del tiempo
            textColor = titleTextColor,
            iconColor = titleTextColor
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Tarjeta de Información General de la Ciudad
        ciudadEncontrada?.let { city ->
            WeatherInfoCard(title = "Tiempo en ${city.name?.decodeUnicodeCompletely() ?: "la capital"}") {
                city.stateSky?.description?.let { estadoCielo ->
                    InfoRowWithIcon(
                        icon = getWeatherIcon(estadoCielo),
                        label = "Cielo:",
                        value = estadoCielo.decodeTildesAVersiAhora(), // O decodeTildes...
                        iconTint = titleTextColor
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    city.temperatures?.max?.let { maxTemp ->
                        TempInfo(
                            icon = Icons.Filled.KeyboardArrowUp,
                            label = "Max:",
                            value = "${maxTemp.decodeTildesAVersiAhora()}º",
                            iconTint = Color(0xFFE57373) // Rojo para max
                        )
                    }
                    city.temperatures?.min?.let { minTemp ->
                        TempInfo(
                            icon = Icons.Filled.KeyboardArrowDown,
                            label = "Min:",
                            value = "${minTemp.decodeTildesAVersiAhora()}º",
                            iconTint = Color(0xFF64B5F6) // Azul para min
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Tarjeta de Comunidad Autónoma
        WeatherInfoCard(title = "Ubicación") {
            InfoRowWithIcon(
                icon = Icons.Filled.Public,
                label = "Comunidad:",
                value = data.provincia.COMUNIDAD_CIUDAD_AUTONOMA.decodeUnicodeCompletely(),
                iconTint = titleTextColor.copy(alpha = 0.7f)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))


        // Tarjeta de Recomendación
        consejoText?.let {
            if (it.isNotBlank() && it != "Sin recomendación disponible.") {
                WeatherInfoCard(title = "Consejo del Día") {
                    InfoRowWithIcon(
                        icon = Icons.Filled.Lightbulb,
                        value = it,
                        iconTint = Color(0xFFFFC107) // Amarillo para consejo
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // Tarjeta de Pronóstico Hoy
        data.today?.p?.let { tiempoHoy ->
            if (tiempoHoy.isNotBlank()) {
                WeatherInfoCard(title = "Pronóstico Detallado - Hoy") {
                    Text(
                        text = tiempoHoy.decodeTildesAVersiAhora(),
                        fontSize = 15.sp,
                        color = cardContentColor,
                        lineHeight = 22.sp
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // Tarjeta de Pronóstico Mañana
        data.tomorrow?.p?.let { tiempoManana ->
            if (tiempoManana.isNotBlank()) {
                WeatherInfoCard(title = "Pronóstico Detallado - Mañana") {
                    Text(
                        text = tiempoManana.decodeTildesAVersiAhora(),
                        fontSize = 15.sp,
                        color = cardContentColor,
                        lineHeight = 22.sp
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // Botón Volver
        StyledAppButton(
            text = "Volver",
            icon = Icons.Filled.ArrowBack,
            onClick = { navController.popBackStack() }
        )
        Spacer(modifier = Modifier.height(16.dp)) // Espacio al final
    }
}

// --- Composables Auxiliares para las Tarjetas y Filas de Información ---

@Composable
fun WeatherInfoCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = cardBackgroundColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = titleTextColor, // O un color más oscuro para el título de la tarjeta
                modifier = Modifier.padding(bottom = 12.dp)
            )
            content()
        }
    }
}

@Composable
fun InfoRowWithIcon(
    icon: ImageVector,
    label: String? = null, // Label es opcional ahora
    value: String,
    iconTint: Color = cardContentColor,
    valueColor: Color = cardContentColor
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
        Icon(
            imageVector = icon,
            contentDescription = label ?: value,
            tint = iconTint,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = buildAnnotatedString {
                label?.let {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold, color = valueColor.copy(alpha = 0.8f))) {
                        append("$it ")
                    }
                }
                withStyle(style = SpanStyle(color = valueColor)) {
                    append(value)
                }
            },
            fontSize = 16.sp
        )
    }
}

@Composable
fun TempInfo(icon: ImageVector, label: String, value: String, iconTint: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = icon, contentDescription = label, tint = iconTint, modifier = Modifier.size(28.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = "$label $value",
            fontSize = 17.sp,
            fontWeight = FontWeight.Medium,
            color = cardContentColor
        )
    }
}


fun getWeatherIcon(description: String): ImageVector {
    val descLower = description.lowercase()
    return when {
        descLower.contains("despejado") || descLower.contains("sol") -> Icons.Outlined.WbSunny
        descLower.contains("cubierto") -> Icons.Filled.Cloud
        descLower.contains("nubes") || descLower.contains("nuboso") -> Icons.Outlined.WbCloudy // O Icons.Filled.CloudQueue
        descLower.contains("lluvia") -> Icons.Filled.Grain // O un icono de lluvia más específico de extended
        descLower.contains("tormenta") -> Icons.Filled.Thunderstorm
        descLower.contains("nieve") -> Icons.Filled.AcUnit
        descLower.contains("niebla") -> Icons.Filled.Dehaze
        else -> Icons.Filled.Thermostat // Icono por defecto
    }
}

//Fin Gemini

//@Composable
//fun Segunda(
//    navController: NavController,
//    viewModel: ProvinciaViewModel = viewModel(), //El viewmodel hace que se carguen las provincias y decide cómo se van a mostrar.
//    provincia: String) { // provincia is CODPROV e.g. "01"
//
//    val db = getDatabase(navController.context)
//    var consejo by remember { mutableStateOf<String?>(null) }
//
//    // LaunchedEffect for fetching data
//    LaunchedEffect(provincia) {
//        viewModel.cargarProvincias(provincia)
//    }
//
//    val provinciaResult: ResultWrapper<Provincias>? by viewModel.provincias.observeAsState()
//
//    // LaunchedEffect for DB operations, should only run on Success
//    LaunchedEffect(provinciaResult) {
//        if (provinciaResult is ResultWrapper.Success) {
//            val successData = (provinciaResult as ResultWrapper.Success<Provincias>).data
//            val nombreProvincia = successData.provincia.NOMBRE_PROVINCIA
//            if (!nombreProvincia.isNullOrEmpty()) {
//                withContext(Dispatchers.IO) {
//                    // Creo un objeto Provincia para insertar
//                    val provinciaAVerSiAhora = Provincia(name = nombreProvincia.decodeUnicodeCompletely()) // Decode here for DB
//
//                    // Inserto la provincia en la base de datos
//                    try {
//                        db.daoProvincia().insertProvincia(provinciaAVerSiAhora)
//                        Log.d("ProvinciaInsercion", "Provincia insertada correctamente: ${provinciaAVerSiAhora.name}")
//                    } catch (e: Exception) {
//                        Log.e("ProvinciaInsercion", "Error al insertar la provincia: ${e.message}")
//                    }
//
//                    //Meto aquí lo segundo que es el segundo insert
//                    val nombreId = db.daoProvincia().getIdByName(nombreProvincia.decodeUnicodeCompletely()) // Use decoded name
//
//                    // Corrected city data access for DB
//                    val provinciaNombreDecodedForDb = successData.provincia.NOMBRE_PROVINCIA.decodeUnicodeCompletely()
//                    val ciudadIdParaCompararForDb = paresProvCod[provinciaNombreDecodedForDb]
//                    val ciudadEncontradaForDb = successData.ciudades?.firstOrNull { ciudad ->
//                        ciudad.id?.get("0") == ciudadIdParaCompararForDb
//                    }
//
//                    val descripcion = ciudadEncontradaForDb?.stateSky?.description
//                    val maxTemp = ciudadEncontradaForDb?.temperatures?.max
//                    val minTemp = ciudadEncontradaForDb?.temperatures?.min
//
//                    val recomendacionProvincia : Recomendacion?
//                    val recomendacionId: Int?
//
//                    if (descripcion?.contains("Cubierto", ignoreCase = true) == true) {
//                        recomendacionProvincia = db.daoRecomendacion().getRandomCommentInRangeRange(1, 10)
//                    } else if (descripcion?.contains("Despejado", ignoreCase = true) == true) {
//                        recomendacionProvincia = db.daoRecomendacion().getRandomCommentInRangeRange(11, 20)
//                    } else if (descripcion?.contains("Nubes", ignoreCase = true) == true) {
//                        recomendacionProvincia = db.daoRecomendacion().getRandomCommentInRangeRange(21, 30)
//                    } else if (descripcion?.contains("Lluvia", ignoreCase = true) == true) {
//                        recomendacionProvincia = db.daoRecomendacion().getRandomCommentInRangeRange(31, 40)
//                    } else if (descripcion?.contains("Soleado", ignoreCase = true) == true) {
//                        recomendacionProvincia = db.daoRecomendacion().getRandomCommentInRangeRange(41, 50)
//                    } else if (descripcion?.contains("Calor", ignoreCase = true) == true) {
//                        recomendacionProvincia = db.daoRecomendacion().getRandomCommentInRangeRange(51, 60)
//                    } else if (descripcion?.contains("Nuboso", ignoreCase = true) == true) {
//                        recomendacionProvincia = db.daoRecomendacion().getRandomCommentInRangeRange(61, 70)
//                    } else {
//                        recomendacionProvincia = db.daoRecomendacion().getRandomCommentInRangeRange(71, 80)
//                    }
//
//                    consejo = recomendacionProvincia?.consejos?.decodeUnicodeCompletely() ?: "Sin recomendación disponible."
//                    recomendacionId = recomendacionProvincia?.id
//
//                    // Creo un objeto Tiempo para insertar
//                    val tiempoAVer = Tiempo(
//                        provincia = nombreProvincia.decodeUnicodeCompletely(), // Use decoded name
//                        provinciaId = nombreId ?: -1, // Fallback if ID not found
//                        descripcion = descripcion ?: "No disponible",
//                        maxTemp = maxTemp?.toDoubleOrNull() ?: 0.0,
//                        minTemp = minTemp?.toDoubleOrNull() ?: 0.0,
//                        Recomendado = consejo ?: "Sin recomendación",
//                        consejoId = recomendacionId ?: -1 // Fallback if ID not found
//                    )
//
//                    // Inserto en la tabla tiempo todo
//                    try {
//                        db.daoTiempo().insertTiempo(tiempoAVer)
//                        Log.d("Tiempo inserción", "Tiempo insertado correctamente para ${tiempoAVer.provincia}.")
//                    } catch (e: Exception) {
//                        Log.e("Tiempo inserción", "Error al insertar tiempo: ${e.message}")
//                    }
//                }
//            } else {
//                Log.d("DBOperations", "El nombre de la provincia es nulo o vacío, no se realizan operaciones de BD.")
//            }
//        }
//    }
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(horizontal = 16.dp),
//        contentAlignment = Alignment.Center
//    ) {
//        when (val result = provinciaResult) {
//            is ResultWrapper.Loading -> {
//                CircularProgressIndicator()
//                Text("Cargando datos...", style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center, modifier = Modifier.padding(top = 70.dp))
//            }
//            is ResultWrapper.Success -> {
//                val successData = result.data
//                Column(
//                    modifier = Modifier
//                        .verticalScroll(rememberScrollState(), enabled = true)
//                        .fillMaxHeight()
//                        .padding(vertical = 16.dp), // Add some vertical padding
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.spacedBy(8.dp)
//                ) {
//                    Text(
//                        text = successData.provincia.NOMBRE_PROVINCIA.decodeUnicodeCompletely(),
//                        style = MaterialTheme.typography.headlineMedium,
//                        color = MaterialTheme.colorScheme.primary,
//                        modifier = Modifier.padding(bottom = 5.dp),
//                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
//                        textAlign = TextAlign.Center
//                    )
//                    Text(
//                        text = buildAnnotatedString {
//                            withStyle(style = androidx.compose.ui.text.SpanStyle(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)) {
//                                append("Comunidad Autónoma: ")
//                            }
//                            append(successData.provincia.COMUNIDAD_CIUDAD_AUTONOMA.decodeUnicodeCompletely())
//                        }
//                    )
//
//                    // Corrected logic for city-specific details
//                    val provinciaNombreDecoded = successData.provincia.NOMBRE_PROVINCIA.decodeUnicodeCompletely()
//                    val ciudadIdParaComparar = paresProvCod[provinciaNombreDecoded]
//
//                    val ciudadEncontrada = successData.ciudades?.firstOrNull { ciudad ->
//                        ciudad.id?.get("0") == ciudadIdParaComparar
//                    }
//
//                    ciudadEncontrada?.let { cityData ->
//                        cityData.stateSky?.description?.let { estadoCielo ->
//                            Text(
//                                text = buildAnnotatedString {
//                                    withStyle(style = androidx.compose.ui.text.SpanStyle(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)) {
//                                        append("Estado del cielo hoy (${cityData.name?.decodeUnicodeCompletely() ?: "ciudad"}): ")
//                                    }
//                                    append(estadoCielo.decodeTildesAVersiAhora())
//                                }
//                            )
//                        }
//                        Spacer(modifier = Modifier.height(5.dp))
//                        cityData.temperatures?.max?.let { maxTemp ->
//                            Text(
//                                text = buildAnnotatedString {
//                                    withStyle(style = androidx.compose.ui.text.SpanStyle(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)) {
//                                        append("Máximas de:  ")
//                                    }
//                                    append("${maxTemp.decodeTildesAVersiAhora()}º")
//                                }
//                            )
//                        }
//                        Spacer(modifier = Modifier.height(5.dp))
//                        cityData.temperatures?.min?.let { minTemp ->
//                            Text(
//                                text = buildAnnotatedString {
//                                    withStyle(style = androidx.compose.ui.text.SpanStyle(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)) {
//                                        append("Mínimas de:  ")
//                                    }
//                                    append("${minTemp.decodeTildesAVersiAhora()}º")
//                                }
//                            )
//                        }
//                    } ?: run {
//                        Text("Datos específicos de la ciudad capital no encontrados.")
//                    }
//
//
//                    Spacer(modifier = Modifier.height(8.dp))
//                    // Muestra la recomendación si está disponible
//                    consejo?.let { consejoText ->
//                        Text(
//                            text = buildAnnotatedString {
//                                withStyle(style = androidx.compose.ui.text.SpanStyle(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)) {
//                                    append("Recomendación:  ")
//                                }
//                                append(consejoText) // consejo is already decoded in LaunchedEffect
//                            }
//                        )
//                    }
//                    Spacer(modifier = Modifier.height(8.dp))
//                    successData.today?.p?.let { tiempo ->
//                        Text(
//                            text = buildAnnotatedString {
//                                withStyle(style = androidx.compose.ui.text.SpanStyle(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)) {
//                                    append("Descripción completa hoy:  ")
//                                }
//                                append(tiempo.decodeTildesAVersiAhora())
//                            }
//                        )
//                    }
//                    Spacer(modifier = Modifier.height(8.dp))
//                    successData.tomorrow?.p?.let { tiempo ->
//                        Text(
//                            text = buildAnnotatedString {
//                                withStyle(style = androidx.compose.ui.text.SpanStyle(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)) {
//                                    append("Descripción completa mañana:  ")
//                                }
//                                append(tiempo.decodeTildesAVersiAhora())
//                            }
//                        )
//                    }
//                    Spacer(modifier = Modifier.height(8.dp)) // Added spacer before button
//                    Button(
//                        onClick = { navController.popBackStack() }, // Simpler navigation
//                        modifier = Modifier.padding(bottom = 16.dp)
//                    ) {
//                        Text("Volver")
//                    }
//                }
//            }
//            is ResultWrapper.Error -> {
//                Column(
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.Center,
//                    modifier = Modifier.fillMaxSize().padding(16.dp)
//                ) {
//                    Text("Error", style = MaterialTheme.typography.headlineSmall, color = Color.Red)
//                    Text(result.message ?: "Ocurrió un error desconocido.", textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyLarge)
//                    result.exception.localizedMessage?.let {
//                        Text("Detalles: $it", style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center, modifier = Modifier.padding(top = 8.dp))
//                    }
//                    Spacer(modifier = Modifier.height(8.dp))
//                    Button(onClick = { navController.popBackStack() }) {
//                        Text("Volver")
//                    }
//                }
//            }
//            null -> {
//                Text("Iniciando...", style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center)
//            }
//        }
//    }
//}
