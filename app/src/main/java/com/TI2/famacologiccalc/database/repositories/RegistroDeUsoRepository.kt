package com.TI2.famacologiccalc.database.repositories

import com.TI2.famacologiccalc.database.dao.RegistroDeUsoDao
import com.TI2.famacologiccalc.database.models.RegistroDeUso
import kotlinx.coroutines.flow.Flow

class RegistroDeUsoRepository(private val registroDeUsoDao: RegistroDeUsoDao) {

    suspend fun insertRegistro(registro: RegistroDeUso) {
        registroDeUsoDao.insertRegistroDeUso(registro)
    }

    fun getRegistrosByUsuarioAndPaciente(usuarioId: Int, pacienteId: Int): Flow<List<RegistroDeUso>> {
        return registroDeUsoDao.getRegistrosByUsuarioAndPaciente(usuarioId, pacienteId)
    }

    suspend fun deleteRegistroById(registroId: Int) {
        registroDeUsoDao.deleteRegistroById(registroId)
    }

    fun getAllRegistros(): Flow<List<RegistroDeUso>> {
        return registroDeUsoDao.getAllRegistros()
    }



}
