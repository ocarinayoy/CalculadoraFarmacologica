package com.TI2.famacologiccalc.database.repositories

import androidx.annotation.WorkerThread
import com.TI2.famacologiccalc.database.dao.PacienteDao
import com.TI2.famacologiccalc.database.models.Pacientes
import kotlinx.coroutines.flow.Flow

class PacienteRepository(private val pacienteDao: PacienteDao) {

    val allPacientes: Flow<List<Pacientes>> = pacienteDao.getAllPacientes()

    // Nueva función para obtener el paciente asociado a un usuario
    fun obtenerPacientePorUsuario(usuarioId: Long): Flow<Pacientes?> {
        return pacienteDao.getPacienteByUsuarioId(usuarioId)
    }

    // Obtener todos los pacientes
    fun obtenerTodosLosPacientes(): Flow<List<Pacientes>> {
        return pacienteDao.getAllPacientes()
    }

    @WorkerThread
    suspend fun insert(paciente: Pacientes) {
        pacienteDao.insertPacientes(paciente)
    }

    @WorkerThread
    suspend fun delete(paciente: Pacientes) {
        pacienteDao.deletePacientes(paciente)
    }

    @WorkerThread
    suspend fun update(paciente: Pacientes) {
        pacienteDao.updatePacientes(paciente)
    }

    // Registrar un nuevo paciente con los nuevos campos
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
