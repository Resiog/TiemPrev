package com.example.quince.room.historialviewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.quince.room.database.AppDatabase.Companion.getDatabase
import com.example.quince.room.dataclasses.Provincia
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HistorialViewModel (application: Application) : AndroidViewModel(application) {
    private val provinciaDao = getDatabase(application).daoProvincia()

    // Esta variable almacena las provincias de la base de datos
    private val _provinciaList = MutableStateFlow<List<Provincia>>(emptyList())
    val provinciaList: StateFlow<List<Provincia>> = _provinciaList

    // Función que obtiene las últimas 5 provincias
    fun cargarUltimasProvincias() {
        viewModelScope.launch(Dispatchers.IO) {
            val provincias = provinciaDao.getUltimasProvincias()
            _provinciaList.value = provincias // Actualiza el estado
        }
    }
}