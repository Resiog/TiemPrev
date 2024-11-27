package com.example.quince.room.daos

import androidx.room.Dao
import androidx.room.Query
import com.example.quince.room.dataclasses.Tiempo

@Dao
interface DaoTiempo {
    @Query("SELECT * FROM tiempo")
    suspend fun getAll(): List<Tiempo>
}