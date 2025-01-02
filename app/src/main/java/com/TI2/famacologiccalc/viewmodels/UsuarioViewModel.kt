package com.TI2.famacologiccalc.viewmodels

import androidx.lifecycle.*
import com.TI2.famacologiccalc.database.models.Usuarios
import com.TI2.famacologiccalc.database.repositories.UsuarioRepository
import kotlinx.coroutines.launch

class UsuarioViewModel(private val repository: UsuarioRepository) : ViewModel() {

    val allUsuarios: LiveData<List<Usuarios>> = repository.allUsuarios.asLiveData()

    // Inserta un nuevo usuario
    fun insert(usuario: Usuarios) = viewModelScope.launch {
        repository.insert(usuario)
    }

    // Elimina un usuario por su ID
    fun delete(id: Long) = viewModelScope.launch {
        // Llamamos al método del repositorio para eliminar por ID
        repository.deleteById(id)
    }

    // Actualiza un usuario
    fun update(usuario: Usuarios) = viewModelScope.launch {
        repository.update(usuario)
    }
}

class UsuarioViewModelFactory(private val repository: UsuarioRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UsuarioViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UsuarioViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
