package com.TI2.famacologiccalc.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class Usuarios(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var nombre: String,
    var email: String,
    var password: String,
    var especialidad: String? = null
)
