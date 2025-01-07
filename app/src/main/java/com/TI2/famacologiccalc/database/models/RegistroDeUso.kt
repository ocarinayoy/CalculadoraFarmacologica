package com.TI2.famacologiccalc.database.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Entity(
    tableName = "registros_de_uso",
    foreignKeys = [
        ForeignKey(entity = Usuarios::class, parentColumns = ["id"], childColumns = ["usuarioId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Pacientes::class, parentColumns = ["id"], childColumns = ["pacienteId"], onDelete = ForeignKey.CASCADE)
    ]
)
data class RegistroDeUso(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val usuarioId: Long,
    val pacienteId: Long,
    val formula: String,
    val resultado: Double,
    val timestamp: String = getCurrentTimestamp() // Usamos una función para obtener el timestamp
) {
    companion object {
        // Función para obtener la fecha en formato ISO 8601
        fun getCurrentTimestamp(): String {
            val currentDateTime = LocalDateTime.now()
            val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
            return currentDateTime.format(formatter)
        }
    }
}
