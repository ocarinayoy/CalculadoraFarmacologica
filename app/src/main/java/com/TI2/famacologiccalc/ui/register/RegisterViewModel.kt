package com.TI2.famacologiccalc.ui.register
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.TI2.famacologiccalc.database.repositories.UsuarioRepository
import com.TI2.famacologiccalc.database.models.Usuarios
import kotlinx.coroutines.launch
class RegisterViewModel(private val repository: UsuarioRepository) : ViewModel() {
    // Variable privada para manejar el estado del registro (mutable)
    private val _registerResult = MutableLiveData<Boolean>()
    // Variable pública para exponer el resultado del registro (inmutable)
    val registerResult: LiveData<Boolean> = _registerResult
    // Función para manejar el registro de un nuevo usuario
    fun register(nombre: String, email: String, password: String, especialidad: String?) {
        // Usamos viewModelScope para manejar la operación en un hilo de fondo
        viewModelScope.launch {
            // Verificamos si el usuario ya existe
            val existingUser = repository.loginUsuario(email, password)
            if (existingUser == null) {
                // Si el usuario no existe, creamos un nuevo usuario y lo insertamos
                val newUser = Usuarios(
                    nombre = nombre,
                    email = email,
                    password = password,
                    especialidad = especialidad
                )
                repository.insert(newUser)
                // Publicamos el resultado (true si se ha registrado correctamente)
                _registerResult.postValue(true)
            } else {
                // Si el usuario ya existe, publicamos el resultado como false
                _registerResult.postValue(false)
            }
        }
    }
}