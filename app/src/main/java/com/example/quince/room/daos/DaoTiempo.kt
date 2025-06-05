package com.example.quince.room.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.quince.room.dataclasses.Provincia
import com.example.quince.room.dataclasses.Recomendacion
import com.example.quince.room.dataclasses.Tiempo

@Dao
interface DaoTiempo {
    @Query("SELECT * FROM tiempo")
    suspend fun getAll(): List<Tiempo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTiempo(tiempo: Tiempo)

    // Obtiene el registro de tiempo asociado a una provincia concreta
    @Query("SELECT * FROM tiempo WHERE provinciaId = :provinciaId LIMIT 1")
    suspend fun getTiempoByProvinciaId(provinciaId: Int): Tiempo?


    @Transaction
    suspend fun insertarRecomendaciónRango(
        tiempo: Tiempo,
        recomendacion: DaoRecomendacion,
    ) {
        // Obtener una recomendación aleatoria entre valores 1 a 3
        val recomendacionAleatoria = recomendacion.getRandomCommentInRange()
        insertTiempo(tiempo)
    }
}