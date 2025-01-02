package com.TI2.famacologiccalc.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.TI2.famacologiccalc.database.repositories.UsuarioRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UsuarioRepository) : ViewModel() {

    // Variable privada para manejar el estado del login (mutable)
    private val _loginResult = MutableLiveData<Boolean>()

    // Variable pública para exponer el resultado del login (inmutable)
    val loginResult: LiveData<Boolean> = _loginResult

    // Función para manejar el login. Recibe email y contraseña como parámetros
    fun login(email: String, password: String) {
        // Usamos viewModelScope para manejar la operación en un hilo de fondo
        viewModelScope.launch {
            // Llamamos al repositorio para verificar si las credenciales son correctas
            val usuario = repository.loginUsuario(email, password)

            // Publicamos el resultado (true si el usuario existe, false si no)
            _loginResult.postValue(usuario != null)
        }
    }


}
