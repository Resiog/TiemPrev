package com.example.quince.retrofit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quince.model.Provincias
import kotlinx.coroutines.launch

class ProvinciaViewModel : ViewModel() {
    private val _provincias = MutableLiveData<Provincias>()
    val provincias: LiveData<Provincias> = _provincias

    fun cargarProvincias(idProvincia: String) {
        viewModelScope.launch {
            try {
                val provincias = clienteRetrofit.apiService.getProvincias(idProvincia)
                _provincias.value = provincias
            } catch (e: Exception) {
                // Manejar errores
            }
        }
    }
}