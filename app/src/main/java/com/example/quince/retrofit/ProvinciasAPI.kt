package com.example.quince.retrofit

import com.example.quince.model.Provincias
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProvinciasAPI {
    @GET("provincias/{codigo}")
    suspend fun getProvincias(@Path("codigo") codigo: String): Provincias
}

