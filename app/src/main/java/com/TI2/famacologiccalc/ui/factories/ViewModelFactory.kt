package com.TI2.famacologiccalc.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.TI2.famacologiccalc.database.repositories.UsuarioRepository
import com.TI2.famacologiccalc.ui.login.LoginViewModel
import com.TI2.famacologiccalc.ui.register.RegisterViewModel


class ViewModelFactory (private val repository: UsuarioRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return when {
                modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(repository) as T
                modelClass.isAssignableFrom(RegisterViewModel::class.java) -> RegisterViewModel(repository) as T
                else -> throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
}
