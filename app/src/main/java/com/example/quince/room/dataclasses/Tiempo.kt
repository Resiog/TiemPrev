package com.example.quince.room.dataclasses

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "tiempo",
    foreignKeys = [
        ForeignKey(
            entity = Provincia::class,
            parentColumns = ["id"],
            childColumns = ["provinciaId"]
        ),
        ForeignKey(
            entity = Recomendacion::class,
            parentColumns = ["id"],
            childColumns = ["consejoId"]
        )
    ]
)
data class Tiempo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "provincia")
    val provincia: String,
    @ColumnInfo(name = "provinciaId")
    val provinciaId: Int,
    @ColumnInfo(name = "descripcion")
    val descripcion: String,
    @ColumnInfo(name = "maxTemp")
    val maxTemp: Double,
    @ColumnInfo(name = "minTemp")
    val minTemp: Double,
    @ColumnInfo(name = "recomendado")
    val Recomendado: String,
    @ColumnInfo(name = "consejoId")
    val consejoId: Int
)
