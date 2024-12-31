package com.TI2.famacologiccalc.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.TI2.famacologiccalc.database.models.Usuarios
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {

    // Obtener todos los usuarioss
    @Query("SELECT * FROM usuarios")
    fun getAllUsuarios(): Flow<List<Usuarios>>

    // Insertar un nuevo usuarios
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUsuarios(usuarios: Usuarios)

    // Actualizar un usuarios existente
    @Update
    suspend fun updateUsuarios(usuarios: Usuarios)

    // Eliminar un usuarios
    @Delete
    suspend fun deleteUsuarios(usuarios: Usuarios)

    // Buscar usuarios por su email
    @Query("SELECT * FROM usuarios WHERE email = :email LIMIT 1")
    suspend fun getUsuarioByEmail(email: String): Usuarios?
}