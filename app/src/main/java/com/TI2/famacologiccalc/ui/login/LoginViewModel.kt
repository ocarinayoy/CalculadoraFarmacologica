package com.TI2.famacologiccalc.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.TI2.famacologiccalc.database.models.Usuarios
import com.TI2.famacologiccalc.database.repositories.UsuarioRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UsuarioRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<Boolean>()
    val loginResult: LiveData<Boolean> = _loginResult

    private val _usuarioLogueado = MutableLiveData<Usuarios?>()
    val usuarioLogueado: LiveData<Usuarios?> = _usuarioLogueado

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val usuario = repository.loginUsuario(email, password)
            if (usuario != null) {
                _loginResult.postValue(true)
                _usuarioLogueado.postValue(usuario) // Guardar usuario logeado
            } else {
                _loginResult.postValue(false)
                _usuarioLogueado.postValue(null) // Borrar cualquier usuario previo
            }
        }
    }

    fun logout() {
        _usuarioLogueado.postValue(null) // Limpiar usuario logeado
    }
}




