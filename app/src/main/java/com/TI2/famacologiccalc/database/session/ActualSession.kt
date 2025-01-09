package com.TI2.famacologiccalc.database.session

import com.TI2.famacologiccalc.database.models.Usuarios

object ActualSession {
    var usuarioLogeado: Usuarios? = null
    var isLoggedIn: Boolean = false
}

