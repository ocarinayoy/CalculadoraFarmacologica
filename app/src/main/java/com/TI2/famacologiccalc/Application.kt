package com.TI2.famacologiccalc

import android.app.Application
import com.TI2.famacologiccalc.database.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class Application : Application() {
    // Scope para las operaciones de la base de datos
    val applicationScope = CoroutineScope(SupervisorJob())

    // Base de datos inicializada con el scope
    val database by lazy { AppDatabase.getDatabase(this, applicationScope) }
}
