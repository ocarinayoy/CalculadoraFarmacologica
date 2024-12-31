package com.TI2.famacologiccalc.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class Usuarios(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val email: String,
    val password: String,
    val especialidad: String? = null
)
