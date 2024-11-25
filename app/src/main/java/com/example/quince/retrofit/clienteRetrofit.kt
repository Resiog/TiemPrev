package com.example.quince.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object clienteRetrofit {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.el-tiempo.net/api/json/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService = retrofit.create(ProvinciasAPI::class.java)
}