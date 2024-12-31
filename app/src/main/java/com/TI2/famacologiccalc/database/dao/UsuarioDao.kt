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

    // Obtener todos los usuarios
    @Query("SELECT * FROM usuarios")
    fun getAllUsuarios(): Flow<List<Usuarios>>

    // Insertar un nuevo usuario
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUsuarios(usuarios: Usuarios)

    // Actualizar un usuario existente
    @Update
    suspend fun updateUsuarios(usuarios: Usuarios)

    // Eliminar un usuario
    @Delete
    suspend fun deleteUsuarios(usuarios: Usuarios)

    // Buscar usuario por su email
    @Query("SELECT * FROM usuarios WHERE email = :email LIMIT 1")
    suspend fun getUsuarioByEmail(email: String): Usuarios?

    // Buscar usuario por su email y contraseña
    @Query("SELECT * FROM usuarios WHERE email = :email AND password = :password LIMIT 1")
    suspend fun getUsuarioByEmailAndPassword(email: String, password: String): Usuarios?
}
