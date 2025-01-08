package com.TI2.famacologiccalc.database.repositories

import androidx.annotation.WorkerThread
import com.TI2.famacologiccalc.database.dao.PacienteDao
import com.TI2.famacologiccalc.database.models.Pacientes
import kotlinx.coroutines.flow.Flow

class PacienteRepository(private val pacienteDao: PacienteDao) {

    // Obtener todos los pacientes
    val allPacientes: Flow<List<Pacientes>> = pacienteDao.getAllPacientes()

    // Obtener todos los pacientes registrados por un usuario específico
    fun obtenerPacientesPorUsuario(usuarioId: Long): Flow<List<Pacientes>> {
        return pacienteDao.getPacientesByUsuarioId(usuarioId)
    }

    // Insertar un nuevo paciente
    @WorkerThread
    suspend fun insert(paciente: Pacientes) {
        pacienteDao.insertPacientes(paciente)
    }

    // Eliminar un paciente
    @WorkerThread
    suspend fun delete(paciente: Pacientes) {
        pacienteDao.deletePacientes(paciente)
    }

    // Actualizar un paciente
    @WorkerThread
    suspend fun update(paciente: Pacientes) {
        pacienteDao.updatePacientes(paciente)
    }

    // Registrar un nuevo paciente con los campos completos
    @WorkerThread
    suspend fun registrarPaciente(
        nombre: String,
        edad: Int,
        peso: Double?,
        altura: Double?,
        fechaRegistro: String,
        estatus: String,
        usuarioId: Long
    ) {
        val paciente = Pacientes(
            nombre = nombre,
            edad = edad,
            peso = peso,
            altura = altura,
            fechaRegistro = fechaRegistro,
            estatus = estatus,
            usuarioId = usuarioId
        )
        insert(paciente)
    }
}
