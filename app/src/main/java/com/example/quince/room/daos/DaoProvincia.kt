package com.example.quince.room.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.quince.room.dataclasses.Provincia

@Dao
interface DaoProvincia {
    @Query("SELECT * FROM provincia")
    suspend fun getAll(): List<Provincia>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProvincia(provincia: Provincia)

    @Query("SELECT * FROM provincia WHERE name = :provinciaName LIMIT 1")
    suspend fun getProvinciaByName(provinciaName: String): Provincia?

    // Función para obtener solo el ID de la provincia por su nombre
    @Query("SELECT id FROM provincia WHERE name = :provinciaName LIMIT 1")
    suspend fun getIdByName(provinciaName: String): Int?
}