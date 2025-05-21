package com.example.quince.retrofit

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quince.model.Provincias
import com.example.quince.model.ResultWrapper // Import the new wrapper
import com.example.quince.room.database.AppDatabase
import com.example.quince.room.database.AppDatabase.Companion.getDatabase
import com.example.quince.room.dataclasses.Recomendacion
import kotlinx.coroutines.launch

class ProvinciaViewModel : ViewModel() { //El viewmodel decide la lógica de visualización de los datos.
    private val _provincias = MutableLiveData<ResultWrapper<Provincias>>() //Aquí se almacena la información de las provincias. // Changed type
    val provincias: LiveData<ResultWrapper<Provincias>> = _provincias //Esto convierte el MutableLiveData en un LiveData // Changed type

    fun cargarProvincias(idProvincia: String) {
        _provincias.value = ResultWrapper.Loading // Set Loading state before launch
        viewModelScope.launch {
            try {
                val provinciasData = clienteRetrofit.apiService.getProvincias(idProvincia)
                _provincias.value = ResultWrapper.Success(provinciasData) // Set Success state
            } catch (e: Exception) {
                Log.e("ProvinciaViewModel", "Error cargando provincias para id: $idProvincia", e) // Updated Log
                _provincias.value = ResultWrapper.Error(e, "No se pudieron cargar los datos de la provincia (ID: $idProvincia). Verifique su conexión o inténtelo más tarde.") // Set Error state
            }
        }
    }
}