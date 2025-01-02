package com.TI2.famacologiccalc.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.TI2.famacologiccalc.database.dao.PacienteDao
import com.TI2.famacologiccalc.database.dao.UsuarioDao
import com.TI2.famacologiccalc.database.models.Pacientes
import com.TI2.famacologiccalc.database.models.Usuarios
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Usuarios::class, Pacientes::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
    abstract fun pacienteDao(): PacienteDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "famacologic_calc_database"
                )
                    .addCallback(DatabaseCallback(scope)) // Agregamos el Callback
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.usuarioDao())
                }
            }
        }

        suspend fun populateDatabase(usuarioDao: UsuarioDao) {
            // Insertamos datos de prueba en la tabla Usuarios
            usuarioDao.insertUsuarios(Usuarios(1, "admin","Admin", "root"))
            usuarioDao.insertUsuarios(Usuarios(2, "Sergio","ocarinayoy12@gmail.com", "contraseña"))
        }
    }
}
