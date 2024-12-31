package com.TI2.famacologiccalc.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.TI2.famacologiccalc.database.dao.PacienteDao
import com.TI2.famacologiccalc.database.dao.UsuarioDao
import com.TI2.famacologiccalc.database.models.Pacientes
import com.TI2.famacologiccalc.database.models.Usuarios


@Database(entities = [Usuarios::class, Pacientes::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
    abstract fun pacienteDao(): PacienteDao
}
