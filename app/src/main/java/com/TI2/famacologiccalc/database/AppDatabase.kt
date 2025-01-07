package com.TI2.famacologiccalc.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.TI2.famacologiccalc.database.dao.PacienteDao
import com.TI2.famacologiccalc.database.dao.RegistroDeUsoDao
import com.TI2.famacologiccalc.database.dao.UsuarioDao
import com.TI2.famacologiccalc.database.models.Pacientes
import com.TI2.famacologiccalc.database.models.RegistroDeUso
import com.TI2.famacologiccalc.database.models.Usuarios

@Database(entities = [Usuarios::class, Pacientes::class, RegistroDeUso::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // Abstract methods to access DAOs
    abstract fun usuarioDao(): UsuarioDao
    abstract fun pacienteDao(): PacienteDao
    abstract fun registrosDeUsoDao(): RegistroDeUsoDao

    companion object {
        // Volatile ensures that multiple threads handle the INSTANCE correctly
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Singleton pattern to ensure only one instance of the database is created
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                // If no instance exists, create one
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "famacologic_calc_database" // Database name
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
