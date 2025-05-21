package com.example.quince.pantallas

import android.graphics.drawable.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.quince.mapa.mapeadoProvincias
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.graphics.vector.ImageVector

//Esta primera pantalla lo único que hace es mandar a la segunda por lo que no hace falta usar el viewmodel de retrofit
//Solo se usa el navController

//Modificación dada por Gemini

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Primera(
    navController: NavController,
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("") }
    val provincias = listOf(
        "A Coruña", "Alicante", "Albacete", "Almería", "Álava", "Asturias", "Ávila", "Badajoz",
        "Barcelona", "Bizkaia", "Burgos", "Cáceres", "Cádiz", "Cantabria", "Castellón", "Ceuta",
        "Ciudad Real", "Córdoba", "Cuenca", "Gipuzkoa", "Girona", "Granada", "Guadalajara",
        "Huelva", "Huesca", "Illes Balears", "Jaén", "La Rioja", "Las Palmas", "León", "Lleida",
        "Lugo", "Madrid", "Málaga", "Melilla", "Murcia", "Navarra", "Ourense", "Palencia",
        "Pontevedra", "Salamanca", "Santa Cruz de Tenerife", "Segovia", "Sevilla", "Soria",
        "Tarragona", "Teruel", "Toledo", "València", "Valladolid", "Zamora", "Zaragoza"
    ).sorted() // Es bueno tenerlas ordenadas alfabéticamente

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(gradientStartColor, gradientEndColor) // Mismo degradado
                )
            ),
        contentAlignment = Alignment.TopCenter // Alineamos el contenido principal arriba
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp) // Espacio entre elementos
        ) {
            // Título estilizado
            ScreenTitle(
                text = "Elige una Provincia",
                icon = Icons.Filled.LocationOn, // Icono temático
                iconColor = titleTextColor,
                textColor = titleTextColor
            )

            // Dropdown estilizado
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth(0.9f) // Que ocupe un buen ancho
            ) {
                OutlinedTextField(
                    value = selectedText,
                    onValueChange = {}, // No se cambia directamente
                    readOnly = true,
                    label = { Text("Provincia") },
                    placeholder = { Text(text = "Selecciona una provincia...") },
                    leadingIcon = {
                        Icon(
                            Icons.Filled.LocationOn,
                            contentDescription = "Icono de provincia",
                            tint = MaterialTheme.colorScheme.primary // O buttonBackgroundColor
                        )
                    },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .menuAnchor() // Importante para ExposedDropdownMenuBox
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp), // Bordes redondeados
                    colors = TextFieldDefaults.outlinedTextFieldColors( // Colores personalizados
                        focusedBorderColor = MaterialTheme.colorScheme.primary, // O buttonBackgroundColor
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.7f),
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f) // Fondo ligeramente transparente
                    )
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant) // Fondo del menú desplegable
                ) {
                    provincias.forEach { provincia ->
                        DropdownMenuItem(
                            text = { Text(text = provincia, color = MaterialTheme.colorScheme.onSurfaceVariant) },
                            onClick = {
                                selectedText = provincia
                                expanded = false
                            },
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
                        )
                    }
                }
            }

            // Botón "Consultar" estilizado
            // Lo colocamos más abajo con un Spacer si es necesario, o dejamos que Arrangement.spacedBy haga su trabajo
            // Si quieres que esté siempre abajo:
            // Spacer(modifier = Modifier.weight(1f)) // Empuja el botón hacia abajo
            StyledAppButton( // Usamos el mismo estilo de botón que en Principal
                text = "Consultar Tiempo",
                icon = Icons.Filled.Check, // Un icono diferente, por ejemplo
                onClick = {
                    val provinciaValue = mapeadoProvincias[selectedText]
                    if (provinciaValue != null) {
                        navController.navigate("segundaPantalla/$provinciaValue")
                    }
                },
                // El botón se habilita solo si se ha seleccionado una provincia
                enabled = selectedText.isNotBlank()
            )
        }
    }
}

// Composable reutilizable para el título (similar al de la pantalla principal)
@Composable
fun ScreenTitle(text: String, icon: ImageVector, iconColor: Color, textColor: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(top = 20.dp, bottom = 20.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Icono de título",
            tint = iconColor,
            modifier = Modifier.size(60.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = text,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            letterSpacing = 1.sp
        )
    }
}

// El StyledAppButton que definimos para la pantalla principal.
// Asegúrate de que este composable esté accesible (en el mismo archivo o importado).
// Si no lo tienes, aquí está una versión (ajústala si la tuya es diferente):
@Composable
fun StyledAppButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    backgroundColor: Color = buttonBackgroundColor,
    contentColor: Color = buttonContentColor,
    enabled: Boolean = true // Para habilitar/deshabilitar el botón
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .height(60.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = contentColor,
            disabledContainerColor = backgroundColor.copy(alpha = 0.5f), // Color cuando está deshabilitado
            disabledContentColor = contentColor.copy(alpha = 0.7f)
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 6.dp,
            pressedElevation = 2.dp,
            disabledElevation = 0.dp
        ),
        enabled = enabled // Aquí se aplica el estado de habilitado
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = text,
                fontWeight = FontWeight.Medium,
                fontSize = 17.sp
            )
        }
    }
}

//Fin modificación dada por Gemini

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun Primera(
//    navController: NavController,
//) {
//    var expanded by remember { mutableStateOf(false) }
//    var selectedText by remember { mutableStateOf("") }
//    val provincias = listOf(
//        "A Coruña", "Alicante", "Albacete", "Almería", "Álava", "Asturias", "Ávila", "Badajoz",
//        "Barcelona", "Bizkaia", "Burgos", "Cáceres", "Cádiz", "Cantabria", "Castellón", "Ceuta",
//        "Ciudad Real", "Córdoba", "Cuenca", "Gipuzkoa", "Girona", "Granada", "Guadalajara",
//        "Huelva", "Huesca", "Illes Balears", "Jaén", "La Rioja", "Las Palmas", "León", "Lleida",
//        "Lugo", "Madrid", "Málaga", "Melilla", "Murcia", "Navarra", "Ourense", "Palencia",
//        "Pontevedra", "Salamanca", "Santa Cruz de Tenerife", "Segovia", "Sevilla", "Soria",
//        "Tarragona", "Teruel", "Toledo", "València", "Valladolid", "Zamora", "Zaragoza"
//    )
//
//    Box(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//            Text(
//                text = "Selecciona tu Provincia",
//                style = MaterialTheme.typography.headlineMedium,
//                fontWeight = FontWeight.Bold,
//                color = MaterialTheme.colorScheme.primary
//            )
//            ExposedDropdownMenuBox(
//                expanded = expanded,
//                onExpandedChange = { expanded = !expanded }
//            ){
//                TextField(
//                    value = selectedText,
//                    onValueChange = {},
//                    readOnly = true,
//                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
//                    placeholder = { Text(text = "Provincia") },
//                    modifier = Modifier.menuAnchor()
//                )
//                ExposedDropdownMenu(
//                    expanded = expanded,
//                    onDismissRequest = { expanded = false }
//                ) {
//                    provincias.forEach { provincia ->
//                        DropdownMenuItem(
//                            text = { Text(text = provincia) },
//                            onClick = {
//                                selectedText = provincia
//                                expanded = false
//                            }
//                        )
//                    }
//                }
//            }
//
//            Button(
//                onClick = {
//                    val provinciaValue = mapeadoProvincias[selectedText]
//                    if (provinciaValue != null) {
//                        navController.navigate("segundaPantalla/$provinciaValue")
//                    }
//                    },
//                modifier = Modifier
//                    .padding(bottom = 16.dp)
//            ) {
//                Text("Consultar")
//            }
//        }
//    }
//}

//Pantalla con diseño cambiado

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrimeraPreview(

) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("") }
    val provincias = listOf(
        "A Coruña", "Alicante", "Albacete", "Almería", "Álava", "Asturias", "Ávila", "Badajoz",
        "Barcelona", "Bizkaia", "Burgos", "Cáceres", "Cádiz", "Cantabria", "Castellón", "Ceuta",
        "Ciudad Real", "Córdoba", "Cuenca", "Gipuzkoa", "Girona", "Granada", "Guadalajara",
        "Huelva", "Huesca", "Illes Balears", "Jaén", "La Rioja", "Las Palmas", "León", "Lleida",
        "Lugo", "Madrid", "Málaga", "Melilla", "Murcia", "Navarra", "Ourense", "Palencia",
        "Pontevedra", "Salamanca", "Santa Cruz de Tenerife", "Segovia", "Sevilla", "Soria",
        "Tarragona", "Teruel", "Toledo", "València", "Valladolid", "Zamora", "Zaragoza"
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Selecciona tu Provincia",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ){
                TextField(
                    value = selectedText,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    placeholder = { Text(text = "Provincia") },
                    modifier = Modifier.menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    provincias.forEach { provincia ->
                        DropdownMenuItem(
                            text = { Text(text = provincia) },
                            onClick = {
                                selectedText = provincia
                                expanded = false
                            }
                        )
                    }
                }
            }

            Button(
                onClick = {
                    //Lo comento para que me salga en el preview
//                    val provinciaValue = mapeadoProvincias[selectedText]
//                    if (provinciaValue != null) {
//                        navController.navigate("segundaPantalla/$provinciaValue")
//                    }
                },
                modifier = Modifier
                    .padding(bottom = 16.dp)
            ) {
                Text("Ok")
            }
        }
    }
}

//@Composable
//@Preview(showBackground = true)
//fun PrimeraPreview2() {
//    PrimeraPreview()
//}
