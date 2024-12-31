package com.TI2.famacologiccalc.database.repositories

import androidx.annotation.WorkerThread
import com.TI2.famacologiccalc.database.dao.PacienteDao
import com.TI2.famacologiccalc.database.models.Pacientes
import kotlinx.coroutines.flow.Flow

class PacienteRepository(private val pacienteDao: PacienteDao) {

    val allPacientes: Flow<List<Pacientes>> = pacienteDao.getAllPacientes()

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
}
