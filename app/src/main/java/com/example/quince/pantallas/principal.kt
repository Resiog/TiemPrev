package com.example.quince.pantallas

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.quince.mapa.mapeadoProvincias

// Definimos los colores que usaremos. Puedes ajustarlos o definirlos en tu Theme.
val gradientStartColor = Color(0xFFEDE7F6) // Lila muy claro (Ejemplo)
val gradientEndColor = Color(0xFFD1C4E9)   // Lila un poco más oscuro (Ejemplo)
val titleTextColor = Color(0xFF4A148C)     // Morado oscuro para el título (Ejemplo)
val buttonBackgroundColor = Color(0xFF7E57C2) // Morado medio para botones (Ejemplo)
val buttonContentColor = Color.White       // Texto e iconos de botones en blanco

fun Color.lighten(fraction: Float = 0.2f): Color = lerp(this, Color.White, fraction)
fun Color.darken(fraction: Float = 0.2f): Color = lerp(this, Color.Black, fraction)

//A partir de aquí son cambios dados por Gemini
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Principal(
    navController: NavController,
) {
    val context = LocalContext.current // Para poder cerrar la actividad

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(gradientStartColor, gradientEndColor)
                )
            ),
        // contentAlignment = Alignment.Center // El Column interno ya se encarga
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize() // Ocupa todo el espacio del Box
                .padding(horizontal = 24.dp, vertical = 32.dp), // Más padding general
            horizontalAlignment = Alignment.CenterHorizontally,
            // Usamos SpaceAround para distribuir el espacio verticalmente
            verticalArrangement = Arrangement.SpaceAround
        ) {
            // Sección del Título Mejorada
            AppTitleComposable()

            // Sección de Botones Mejorada
            ButtonsColumn(navController = navController, context = context as? Activity)
        }
    }
}

@Composable
fun AppTitleComposable() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(bottom = 20.dp) // Espacio debajo del título
    ) {
        Icon(
            imageVector = Icons.Filled.DateRange, // Puedes cambiar este icono
            contentDescription = "Logo TiempoPrev",
            tint = titleTextColor,
            modifier = Modifier.size(70.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "¿Hará bueno hoy?", // Podrías considerar "TiempoPrev" o el nombre completo
            // style = MaterialTheme.typography.headlineLarge, // Puedes usar estilos del tema
            fontSize = 38.sp, // Tamaño ajustado
            fontWeight = FontWeight.ExtraBold, // Más peso
            color = titleTextColor,
            letterSpacing = 1.2.sp // Un poco de espaciado entre letras
        )
        Text(
            text = "Tu pronóstico de confianza", // Subtítulo
            fontSize = 16.sp,
            color = titleTextColor.copy(alpha = 0.8f), // Mismo color pero más tenue
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
fun ButtonsColumn(navController: NavController, context: Activity?) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StyledAppButton(
            text = "Consultar tiempo",
            icon = Icons.Filled.Search,
            onClick = { navController.navigate("primera") }
        )
        Spacer(modifier = Modifier.height(18.dp))
        StyledAppButton(
            text = "Historial de provincias",
            icon = Icons.Filled.Info,
            onClick = { navController.navigate("historial") }
        )
        Spacer(modifier = Modifier.height(18.dp))
        StyledAppButton(
            text = "Salir de la app",
            icon = Icons.Filled.Close,
            onClick = {
                // Forma más correcta de cerrar una app en Android
                context?.finishAffinity() // Cierra esta actividad y todas las de la misma tarea
                // System.exit(0) // Evitar esto si es posible
            }
        )
    }
}

@Composable
fun StyledAppButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    backgroundColor: Color = buttonBackgroundColor, // Usar el color definido arriba
    contentColor: Color = buttonContentColor   // Usar el color definido arriba
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val animatedColor by animateColorAsState(
        targetValue = if (pressed) backgroundColor.lighten(0.35f) else backgroundColor,
        label = "pressColor"
    )
    val animatedContentColor by animateColorAsState(
        targetValue = if (pressed) contentColor.darken(0.35f) else contentColor,
        label = "pressTextColor"
    )

    Button(
        onClick = onClick,
        interactionSource = interactionSource,
        modifier = Modifier
            .fillMaxWidth(0.9f) // Ocupa el 90% del ancho disponible
            .height(60.dp),    // Altura del botón aumentada
        shape = RoundedCornerShape(16.dp), // Bordes más redondeados
        colors = ButtonDefaults.buttonColors(
            containerColor = animatedColor,
            contentColor = animatedContentColor
        ),
        elevation = ButtonDefaults.buttonElevation( // Sombra para efecto "elevado"
            defaultElevation = 6.dp,
            pressedElevation = 2.dp,
            disabledElevation = 0.dp
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center // Centra el contenido del Row si no ocupa todo
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text, // Para accesibilidad
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp)) // Espacio entre icono y texto
            Text(
                text = text,
                fontWeight = FontWeight.Medium,
                fontSize = 17.sp // Tamaño de texto del botón
            )
        }
    }
}




//A partir de aquí es lo que tenía


//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun Principal(
//    navController: NavController,
//) {
//    val context = LocalContext.current // Para poder cerrar la actividad
//
//    Box(
//            modifier = Modifier.fillMaxSize(),
//            contentAlignment = Alignment.Center
//        ) {
//            Column(
//                modifier = Modifier
//                    .fillMaxHeight()
//                    .fillMaxWidth()
//                    .padding(16.dp),
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.Center
//            ) {
//                Row (modifier = Modifier
//                    .fillMaxWidth()
//                    .weight(2f),
//                    horizontalArrangement = Arrangement.Center,
//                    verticalAlignment = Alignment.CenterVertically){
//                    Text(text = "TIEMPREV",
//                        style = MaterialTheme.typography.headlineMedium,
//                        fontWeight = FontWeight.Bold,
//                        color = MaterialTheme.colorScheme.primary,
//                        fontSize = 50.sp)
//                }
//                Row (modifier = Modifier
//                    .fillMaxWidth()
//                    .weight(3f),
//                    horizontalArrangement = Arrangement.Center,
//                    verticalAlignment = Alignment.CenterVertically){
//                    Column {
//                        Button(
//                            modifier = Modifier.fillMaxWidth(),
//                            onClick = {
//                                navController.navigate("primera")
//                            }
//                        ) {
//                            Text("Consultar tiempo")
//                        }
//                        Button(modifier = Modifier.fillMaxWidth(),
//                            onClick = { navController.navigate("historial") }) {
//                            Text("Historial de provincias")
//                        }
//                        Button(modifier = Modifier.fillMaxWidth(),
//                            onClick = {
//                                System.exit(0)
//                            }) {
//                            Text("Salir de la app")
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun PrincipalPreview(
//) {
//    Box(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxHeight()
//                .fillMaxWidth()
//                .padding(16.dp),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
//        ) {
//            Row (modifier = Modifier
//                .fillMaxWidth()
//                .weight(2f),
//                horizontalArrangement = Arrangement.Center,
//                verticalAlignment = Alignment.CenterVertically){
//                Text(text = "TIEMPREV",
//                    style = MaterialTheme.typography.headlineMedium,
//                    fontWeight = FontWeight.Bold,
//                    color = MaterialTheme.colorScheme.primary,
//                    fontSize = 50.sp)
//            }
//            Row (modifier = Modifier
//                .fillMaxWidth()
//                .weight(3f),
//                horizontalArrangement = Arrangement.Center,
//                verticalAlignment = Alignment.CenterVertically){
//                Column {
//                    Button(
//                        modifier = Modifier.fillMaxWidth(),
//                        onClick = {  }) {
//                        Text("Consultar tiempo")
//                    }
//                    Button(modifier = Modifier.fillMaxWidth(),
//                        onClick = { /* Acción del segundo botón */ }) {
//                        Text("Historial de provincias")
//                    }
//                    Button(modifier = Modifier.fillMaxWidth(),
//                        onClick = { /* Acción del tercer botón */ }) {
//                        Text("Salir de la app")
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//@Preview(showBackground = true)
//fun PrincialAVerquetal() {
//    PrincipalPreview()
//}