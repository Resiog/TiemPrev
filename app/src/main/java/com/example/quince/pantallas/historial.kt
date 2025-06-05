package com.example.quince.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.quince.room.dataclasses.Provincia
import com.example.quince.room.historialviewmodel.HistorialViewModel
import com.example.quince.navcontroller.Rutas


//Gemini

val iconColorInCard = titleTextColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Historial(
    navController: NavController,
    viewModel: HistorialViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.cargarUltimasProvincias()
    }

    val provincias by viewModel.provinciaList.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(gradientStartColor, gradientEndColor)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 24.dp, bottom = 16.dp, start = 16.dp, end = 16.dp), // Ajusta padding
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ScreenTitle( // Reutiliza el ScreenTitle
                text = "Historial de Consultas",
                icon = Icons.Filled.History,
                textColor = titleTextColor,
                iconColor = titleTextColor
            )
            Spacer(modifier = Modifier.height(20.dp))

            if (provincias.isEmpty()) {
                EmptyHistoryState()
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f), // Para que ocupe el espacio disponible antes del botón
                    verticalArrangement = Arrangement.spacedBy(12.dp), // Espacio entre tarjetas
                    contentPadding = PaddingValues(bottom = 16.dp) // Espacio para que el último item no pegue con el botón
                ) {
                    items(provincias, key = { provincia -> provincia.id }) { provincia -> // Usa un 'key' si tus provincias tienen ID único
                        HistoryItemCard(provincia = provincia) {
                            navController.navigate("${Rutas.DetalleHistorial.ruta}/${provincia.id}")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp)) // Espacio antes del botón Volver
            StyledAppButton( // Reutiliza tu botón estilizado
                text = "Volver",
                icon = Icons.Filled.ArrowBackIosNew,
                onClick = { navController.popBackStack() }
            )
        }
    }
}

@Composable
fun HistoryItemCard(provincia: Provincia, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 70.dp) // Altura mínima para la tarjeta
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = cardBackgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono a la izquierda
            Icon(
                imageVector = Icons.Outlined.LocationOn, // O Icons.Filled.Place, Icons.Filled.Map
                contentDescription = "Icono de provincia",
                tint = iconColorInCard, // Color del icono dentro de la tarjeta
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape) // Opcional: darle forma circular si el icono se ve bien así
                    .background(iconColorInCard.copy(alpha = 0.1f)) // Fondo sutil para el icono
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Nombre de la provincia
            Text(
                text = provincia.name.decodeUnicodeCompletely(), // Asumo que podrías necesitar decodificar
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = cardContentColor,
                modifier = Modifier.weight(1f) // Para que ocupe el espacio restante
            )

            // Opcional: Podrías añadir un icono de "re-consultar" o una flecha aquí
            // Icon(Icons.Filled.ChevronRight, contentDescription = "Ver detalles")
        }
    }
}

@Composable
fun EmptyHistoryState() {
    Column(
        modifier = Modifier
            .fillMaxSize() // Ocupa el espacio que le da el LazyColumn o el Column padre
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Filled.HourglassEmpty, // O algún icono más "divertido" como SentimentDissatisfied
            contentDescription = "Historial vacío",
            tint = titleTextColor.copy(alpha = 0.6f),
            modifier = Modifier.size(80.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "¡Aún no has consultado nada!",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = titleTextColor.copy(alpha = 0.8f),
            textAlign = TextAlign.Center
        )
        Text(
            text = "Realiza una búsqueda para verla aquí.",
            fontSize = 15.sp,
            color = titleTextColor.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

//Fin Gemini


//@Composable
//fun Historial(
//    navController: NavController,
//    viewModel: HistorialViewModel = viewModel()
//) {
//
//    LaunchedEffect(Unit) {
//        viewModel.cargarUltimasProvincias()
//    }
//
//    val provincias by viewModel.provinciaList.collectAsState()
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(horizontal = 16.dp),
//        contentAlignment = Alignment.TopCenter
//    ) {
//        Column (
//            modifier = Modifier
//                .fillMaxHeight()
//                .fillMaxWidth()
//                .padding(16.dp),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
//        ){
//            Row (modifier = Modifier
//                .weight(2f),
//                verticalAlignment = Alignment.CenterVertically)
//            {
//                Text("Registro de provincias consultadas",
//                    style = MaterialTheme.typography.headlineMedium,
//                    fontWeight = FontWeight.Bold,
//                    color = MaterialTheme.colorScheme.primary,
//                    fontSize = 20.sp)
//            }
//            Column (modifier = Modifier.weight(4f)) {
//                LazyColumn(
//                    modifier = Modifier.fillMaxSize()
//                ) {
//                    items(count = provincias.size) {
//                        val provincia = provincias.getOrNull(it)
//                        if (provincia != null) {
//                            Row(
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .padding(vertical = 5.dp)
//                                    .padding(10.dp)
//                            ) {
//                                Text("Consultada: ",
//                                    style = MaterialTheme.typography.displayLarge,
//                                    fontWeight = FontWeight.Bold,
//                                    color = MaterialTheme.colorScheme.primary,
//                                    fontSize = 20.sp)
//                                Text("${provincia.name}",
//                                    style = MaterialTheme.typography.displayLarge,
//                                    fontWeight = FontWeight.Bold,
//                                    color = Color.Blue,
//                                    fontSize = 30.sp)
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//}

@Composable
fun HistorialPreview(){
    val provincias = listOf(
        "Provincia 1",
        "Provincia 2"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column (modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center)
        {
            Row (modifier = Modifier
                .weight(2f),
                verticalAlignment = Alignment.CenterVertically){
                Text("Últimas provincias consultadas",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 20.sp)
            }
            Column (modifier = Modifier.weight(4f)) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(count = provincias.size) {
                        val provincia = provincias.getOrNull(it)
                        if (provincia != null) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 5.dp)
                                    .padding(10.dp)
                            ) {
                                Text("${provincia}",
                                    style = MaterialTheme.typography.displayLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Blue,
                                    fontSize = 30.sp)
                                Text("${provincia}",
                                    style = MaterialTheme.typography.displayLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Blue,
                                    fontSize = 30.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun HistorialPreviewVista() {
    HistorialPreview()
}



