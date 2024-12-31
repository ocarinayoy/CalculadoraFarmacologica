package com.TI2.famacologiccalc.database.repositories

import androidx.annotation.WorkerThread
import com.TI2.famacologiccalc.database.dao.UsuarioDao
import com.TI2.famacologiccalc.database.models.Usuarios
import kotlinx.coroutines.flow.Flow

class UsuarioRepository(private val usuarioDao: UsuarioDao) {

    val allUsuarios: Flow<List<Usuarios>> = usuarioDao.getAllUsuarios()

    @WorkerThread
    suspend fun insert(usuario: Usuarios) {
        usuarioDao.insertUsuarios(usuario)
    }

    @WorkerThread
    suspend fun delete(usuario: Usuarios) {
        usuarioDao.deleteUsuarios(usuario)
    }

    @WorkerThread
    suspend fun update(usuario: Usuarios) {
        usuarioDao.updateUsuarios(usuario)
    }

    // Función para verificar el login del usuario por email y contraseña
    @WorkerThread
    suspend fun loginUsuario(email: String, password: String): Usuarios? {
        return usuarioDao.getUsuarioByEmailAndPassword(email, password)
    }
}
