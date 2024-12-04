package com.example.quince.pantallas

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
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.quince.mapa.mapeadoProvincias

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Principal(
    navController: NavController,
) {
    Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row (modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically){
                    Text(text = "TIEMPREV",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 50.sp)
                }
                Row (modifier = Modifier
                    .fillMaxWidth()
                    .weight(3f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically){
                    Column {
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                navController.navigate("primera")
                            }
                        ) {
                            Text("Consultar tiempo")
                        }
                        Button(modifier = Modifier.fillMaxWidth(),
                            onClick = { navController.navigate("historial") }) {
                            Text("Historial de provincias")
                        }
                        Button(modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                System.exit(0)
                            }) {
                            Text("Salir de la app")
                        }
                    }
                }
            }
        }
    }


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrincipalPreview(
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row (modifier = Modifier
                .fillMaxWidth()
                .weight(2f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically){
                Text(text = "TIEMPREV",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 50.sp)
            }
            Row (modifier = Modifier
                .fillMaxWidth()
                .weight(3f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically){
                Column {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {  }) {
                        Text("Consultar tiempo")
                    }
                    Button(modifier = Modifier.fillMaxWidth(),
                        onClick = { /* Acción del segundo botón */ }) {
                        Text("Historial de provincias")
                    }
                    Button(modifier = Modifier.fillMaxWidth(),
                        onClick = { /* Acción del tercer botón */ }) {
                        Text("Salir de la app")
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PrincialAVerquetal() {
    PrincipalPreview()
}