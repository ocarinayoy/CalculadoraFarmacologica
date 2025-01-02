package com.TI2.famacologiccalc.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.TI2.famacologiccalc.database.models.Usuarios
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {

    @Insert
    suspend fun insertUsuarios(usuario: Usuarios)

    @Update
    suspend fun updateUsuarios(usuario: Usuarios)

    // Cambiar la función de eliminación para usar el ID
    @Query("DELETE FROM usuarios WHERE id = :id")
    suspend fun deleteUsuariosById(id: Long)

    @Query("SELECT * FROM usuarios WHERE email = :email AND password = :password LIMIT 1")
    suspend fun getUsuarioByEmailAndPassword(email: String, password: String): Usuarios?

    @Query("SELECT * FROM usuarios")
    fun getAllUsuarios(): Flow<List<Usuarios>>
}
