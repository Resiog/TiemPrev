package com.example.quince.pantallas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.quince.navcontroller.Rutas


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Primera(navController: NavController) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("") }
    val provincias = listOf(
        "Alicante", "Valencia", "Castellón", "Madrid",
        "Barcelona", "Sevilla", "Málaga", "Bilbao",
        "Zaragoza", "Murcia", "Granada", "Cádiz",
        "Córdoba", "Valladolid", "Vigo", "Gijón"
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
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

            // Botón "Ok"
            Button(
                onClick = { navController.navigate(Rutas.SegundaPantalla.ruta) },
                modifier = Modifier
                    .padding(bottom = 16.dp)
            ) {
                Text("Ok")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrimeraA() {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("") }
    val provincias = listOf(
        "Alicante", "Valencia", "Castellón", "Madrid",
        "Barcelona", "Sevilla", "Málaga", "Bilbao",
        "Zaragoza", "Murcia", "Granada", "Cádiz",
        "Córdoba", "Valladolid", "Vigo", "Gijón"
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
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
fun PrimeraPreview() {
    PrimeraA()
}