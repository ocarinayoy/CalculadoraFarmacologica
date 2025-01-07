package com.TI2.famacologiccalc.database.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "pacientes",
    foreignKeys = [
        ForeignKey(
            entity = Usuarios::class,
            parentColumns = ["id"],
            childColumns = ["usuarioId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Pacientes(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nombre: String,
    val edad: Int,
    val peso: Double,
    val altura: Double? = null,
    val usuarioId: Long // Clave foránea para el usuario
)
