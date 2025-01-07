package com.TI2.famacologiccalc.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.TI2.famacologiccalc.database.models.RegistroDeUso
import kotlinx.coroutines.flow.Flow

@Dao
interface RegistroDeUsoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRegistroDeUso(registro: RegistroDeUso)

    @Query("SELECT * FROM registros_de_uso WHERE usuarioId = :usuarioId AND pacienteId = :pacienteId")
    fun getRegistrosByUsuarioAndPaciente(usuarioId: Int, pacienteId: Int): Flow<List<RegistroDeUso>>

    @Query("DELETE FROM registros_de_uso WHERE id = :registroId")
    suspend fun deleteRegistroById(registroId: Int)

    @Query("SELECT * FROM registros_de_uso")
    fun getAllRegistros(): Flow<List<RegistroDeUso>>
}
