package com.TI2.famacologiccalc

import android.app.Application
import com.TI2.famacologiccalc.database.AppDatabase

class Application : Application() {
    // Base de datos inicializada sin el scope
    val database by lazy { AppDatabase.getDatabase(this) }
}
