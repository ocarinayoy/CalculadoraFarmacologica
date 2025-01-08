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

    // Obtener todos los pacientes registrados por un usuario específico
    @Query("SELECT * FROM pacientes WHERE usuarioId = :usuarioId")
    fun getPacientesByUsuarioId(usuarioId: Long): Flow<List<Pacientes>>

    // Obtener todos los pacientes
    @Query("SELECT * FROM pacientes")
    fun getAllPacientes(): Flow<List<Pacientes>>

    // Insertar un nuevo paciente
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPacientes(pacientes: Pacientes)

    // Actualizar un paciente existente
    @Update
    suspend fun updatePacientes(pacientes: Pacientes)

    // Eliminar un paciente
    @Delete
    suspend fun deletePacientes(pacientes: Pacientes)

    // Buscar paciente por ID
    @Query("SELECT * FROM pacientes WHERE id = :id LIMIT 1")
    suspend fun getPacienteById(id: Long): Pacientes?

    // Buscar paciente por usuarioId
    @Query("SELECT * FROM pacientes WHERE usuarioId = :usuarioId LIMIT 1")
    fun getPacienteByUsuarioId(usuarioId: Long): Flow<Pacientes?>
}
