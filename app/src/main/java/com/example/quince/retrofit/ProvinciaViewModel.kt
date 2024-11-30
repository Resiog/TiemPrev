package com.example.quince.retrofit

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quince.model.Provincias
import com.example.quince.room.database.AppDatabase
import com.example.quince.room.database.AppDatabase.Companion.getDatabase
import com.example.quince.room.dataclasses.Recomendacion
import kotlinx.coroutines.launch

class ProvinciaViewModel : ViewModel() { //El viewmodel decide la lógica de visualización de los datos.
    private val _provincias = MutableLiveData<Provincias>() //Aquí se almacena la información de las provincias.
    val provincias: LiveData<Provincias> = _provincias //Esto convierte el MutableLiveData en un LiveData

    fun cargarProvincias(idProvincia: String) {
        viewModelScope.launch {
            try {
                val provincias = clienteRetrofit.apiService.getProvincias(idProvincia)
                _provincias.value = provincias
            } catch (e: Exception) {
                Log.e("ProvinciaViewModel", "Error cargando provincias", e)
            }
        }
    }
}