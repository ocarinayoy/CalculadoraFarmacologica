package com.TI2.famacologiccalc.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.TI2.famacologiccalc.database.models.Pacientes
import kotlinx.coroutines.flow.Flow

@Dao
interface PacienteDao {

    // Obtener todos los pacientes
    @Query("SELECT * FROM pacientes")
    fun getAllPacientes(): Flow<List<Pacientes>>

    // Insertar un nuevo pacientes
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPacientes(pacientes: Pacientes)

    // Actualizar un pacientes existente
    @Update
    suspend fun updatePacientes(pacientes: Pacientes)

    // Eliminar un pacientes
    @Delete
    suspend fun deletePacientes(pacientes: Pacientes)

    // Buscar pacientes por ID
    @Query("SELECT * FROM pacientes WHERE id = :id LIMIT 1")
    suspend fun getPacienteById(id: Int): Pacientes?
}