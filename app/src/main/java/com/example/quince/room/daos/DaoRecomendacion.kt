package com.example.quince.room.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.quince.room.dataclasses.Recomendacion

@Dao
interface DaoRecomendacion {
    @Query("SELECT * FROM recomendacion")
    suspend fun getAll(): List<Recomendacion>

    @Query("SELECT * FROM recomendacion WHERE id BETWEEN 1 AND 8 ORDER BY RANDOM() LIMIT 1") //El limit 1 es para que solo devuelva un valor
    fun getRandomCommentInRange(): Recomendacion?

    // Función para obtener solo el ID de una recomendación
    @Query("SELECT id FROM recomendacion WHERE id = :recomendacionId LIMIT 1")
    suspend fun getIdByRecomendacionId(recomendacionId: Int): Int?
}