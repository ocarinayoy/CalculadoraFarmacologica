package com.TI2.famacologiccalc.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.TI2.famacologiccalc.database.repositories.UsuarioRepository
import com.TI2.famacologiccalc.ui.settings.SettingsViewModel
import com.TI2.famacologiccalc.ui.login.LoginViewModel
import com.TI2.famacologiccalc.ui.register.RegisterViewModel

class ViewModelFactory(
    private val usuarioRepository: UsuarioRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(usuarioRepository) as T
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> RegisterViewModel(usuarioRepository) as T
            modelClass.isAssignableFrom(SettingsViewModel::class.java) -> SettingsViewModel(usuarioRepository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
