package com.TI2.famacologiccalc.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(
    tableName = "pacientes",
    foreignKeys = [
        ForeignKey(
            entity = Usuarios::class,
            parentColumns = ["id"],
            childColumns = ["usuario_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Pacientes(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val edad: Int,
    val peso: Float,
    val usuario_id: Int
)
