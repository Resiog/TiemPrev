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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecomendacion(recomendacion: Recomendacion)

    @Query("SELECT * FROM recomendacion ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomConsejo(): Recomendacion?
}