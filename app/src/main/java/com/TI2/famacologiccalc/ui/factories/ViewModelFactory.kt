package com.TI2.famacologiccalc.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.TI2.famacologiccalc.database.repositories.PacienteRepository
import com.TI2.famacologiccalc.database.repositories.UsuarioRepository
import com.TI2.famacologiccalc.ui.settings.SettingsViewModel
import com.TI2.famacologiccalc.ui.login.LoginViewModel
import com.TI2.famacologiccalc.ui.register.RegisterViewModel
import com.TI2.famacologiccalc.viewmodels.PacienteViewModel
import com.TI2.famacologiccalc.ui.weighteddosage.WeightedDosageViewModel

class ViewModelFactory(
    private val usuarioRepository: UsuarioRepository?,
    private val pacienteRepository: PacienteRepository?
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(PacienteViewModel::class.java) && pacienteRepository != null -> {
                PacienteViewModel(pacienteRepository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) && usuarioRepository != null -> {
                LoginViewModel(usuarioRepository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) && usuarioRepository != null -> {
                RegisterViewModel(usuarioRepository) as T
            }
            modelClass.isAssignableFrom(SettingsViewModel::class.java) && usuarioRepository != null -> {
                SettingsViewModel(usuarioRepository) as T
            }
            modelClass.isAssignableFrom(WeightedDosageViewModel::class.java) && usuarioRepository != null && pacienteRepository != null -> {
                WeightedDosageViewModel(usuarioRepository, pacienteRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class or missing repository")
        }
    }
}
