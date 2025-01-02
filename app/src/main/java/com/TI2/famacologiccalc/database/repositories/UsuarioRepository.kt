package com.TI2.famacologiccalc.database.repositories

import androidx.annotation.WorkerThread
import com.TI2.famacologiccalc.database.dao.UsuarioDao
import com.TI2.famacologiccalc.database.models.Usuarios
import kotlinx.coroutines.flow.Flow

class UsuarioRepository(private val usuarioDao: UsuarioDao) {

    val allUsuarios: Flow<List<Usuarios>> = usuarioDao.getAllUsuarios()

    // Función para insertar un nuevo usuario (registro)
    @WorkerThread
    suspend fun insert(usuario: Usuarios) {
        usuarioDao.insertUsuarios(usuario)
    }

    // Función para eliminar un usuario por su ID
    @WorkerThread
    suspend fun deleteById(id: Long) {
        usuarioDao.deleteUsuariosById(id)
    }

    // Función para actualizar un usuario
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
