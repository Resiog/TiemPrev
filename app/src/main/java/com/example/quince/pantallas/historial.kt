package com.example.quince.pantallas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.quince.room.dataclasses.Provincia
import com.example.quince.room.historialviewmodel.HistorialViewModel


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
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column (
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Row (modifier = Modifier
                .weight(2f),
                verticalAlignment = Alignment.CenterVertically)
            {
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
                                Text("Provincia: ",
                                    style = MaterialTheme.typography.displayLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontSize = 20.sp)
                                Text("${provincia.name}",
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



