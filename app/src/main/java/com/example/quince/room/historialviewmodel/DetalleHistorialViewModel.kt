package com.example.quince.room.historialviewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.quince.room.database.AppDatabase.Companion.getDatabase
import com.example.quince.room.dataclasses.Tiempo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetalleHistorialViewModel(application: Application) : AndroidViewModel(application) {
    private val tiempoDao = getDatabase(application).daoTiempo()

    private val _tiempo = MutableStateFlow<Tiempo?>(null)
    val tiempo: StateFlow<Tiempo?> = _tiempo

    fun cargarTiempoPorProvinciaId(provinciaId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val data = tiempoDao.getTiempoByProvinciaId(provinciaId)
            _tiempo.value = data
        }
    }
}
