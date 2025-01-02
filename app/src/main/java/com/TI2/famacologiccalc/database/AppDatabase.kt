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
import kotlinx.coroutines.flow.onEmpty
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
                    .addCallback(DatabaseCallback(scope)) // Llamamos al callback
                    .build()
                INSTANCE = instance

                // Llamamos explícitamente al método para insertar datos de prueba si no existen
                scope.launch {
                    insertSampleData(instance)
                }

                instance
            }
        }

        suspend fun insertSampleData(database: AppDatabase) {
            val usuarioDao = database.usuarioDao()

            // Verificamos si ya hay usuarios para no insertar duplicados
            val existingUsers = usuarioDao.getAllUsuarios()  // Asumiendo que tienes un método `getAllUsuarios` en tu DAO

            // Usamos la propiedad size para comprobar si la lista está vacía
                // Insertamos datos de prueba solo si no hay usuarios
                val usuario1 = Usuarios(id = 1, nombre = "a", email = "a@a.com", password = "a")
                val usuario2 = Usuarios(id = 2, nombre = "testUser", email = "test@user.com", password = "test123")

                usuarioDao.insertUsuarios(usuario1)
                usuarioDao.insertUsuarios(usuario2)
        }


        private class DatabaseCallback(private val scope: CoroutineScope) :
            RoomDatabase.Callback() {

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // Ya no insertamos aquí, porque lo hacemos en la función insertSampleData
            }
        }
    }

}