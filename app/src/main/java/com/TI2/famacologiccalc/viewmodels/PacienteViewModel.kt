package com.TI2.famacologiccalc.viewmodels

import androidx.lifecycle.*
import com.TI2.famacologiccalc.database.models.Pacientes
import com.TI2.famacologiccalc.database.repositories.PacienteRepository
import kotlinx.coroutines.launch

class PacienteViewModel(private val repository: PacienteRepository) : ViewModel() {

    val allPacientes: LiveData<List<Pacientes>> = repository.allPacientes.asLiveData()

    fun insert(paciente: Pacientes) = viewModelScope.launch {
        repository.insert(paciente)
    }

    fun delete(paciente: Pacientes) = viewModelScope.launch {
        repository.delete(paciente)
    }

    fun update(paciente: Pacientes) = viewModelScope.launch {
        repository.update(paciente)
    }
}

class PacienteViewModelFactory(private val repository: PacienteRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PacienteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PacienteViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
