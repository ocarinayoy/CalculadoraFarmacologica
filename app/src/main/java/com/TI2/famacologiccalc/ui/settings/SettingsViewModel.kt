// SettingsViewModel.kt
package com.TI2.famacologiccalc.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.TI2.famacologiccalc.database.models.Usuarios
import com.TI2.famacologiccalc.database.repositories.UsuarioRepository
import com.TI2.famacologiccalc.sesion.ActualSession
import kotlinx.coroutines.launch

class SettingsViewModel(private val usuarioRepository: UsuarioRepository) : ViewModel() {

    private val _username = MutableLiveData<String>()
    val username: LiveData<String> get() = _username

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> get() = _email

    private val _especialidad = MutableLiveData<String?>()
    val especialidad: LiveData<String?> get() = _especialidad

    private val _contraseña = MutableLiveData<String?>()
    val contraseña: LiveData<String?> get() = _contraseña

    init {
        // Cargar los datos iniciales del usuario logeado
        loadUserInfo()
    }

    private fun loadUserInfo() {
        val usuarioLogeado = ActualSession.usuarioLogeado
        if (usuarioLogeado != null) {
            _username.value = usuarioLogeado.nombre
            _email.value = usuarioLogeado.email
            _especialidad.value = usuarioLogeado.especialidad
            _contraseña.value = usuarioLogeado.password
        } else {
            // Manejo del caso donde no hay usuario logeado
            _username.value = "Usuario no disponible"
            _email.value = "Correo no disponible"
            _especialidad.value = null
        }
    }

    fun updateUserInfo(username: String, email: String, especialidad: String, password: String) {
        // Actualizar los datos en el LiveData
        _username.value = username
        _email.value = email
        _especialidad.value = especialidad

        // Persistir los cambios en la base de datos
        val usuarioLogeado = ActualSession.usuarioLogeado
        if (usuarioLogeado != null) {
            usuarioLogeado.nombre = username
            usuarioLogeado.email = email
            usuarioLogeado.especialidad = especialidad
            usuarioLogeado.password = password

            // Actualizar en la base de datos usando el repositorio
            viewModelScope.launch {
                usuarioRepository.update(usuarioLogeado)
            }
        }
    }

}
