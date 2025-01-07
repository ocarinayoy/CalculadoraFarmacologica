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

    // Nueva función para registrar un paciente
    fun registrarPaciente(
        nombre: String,
        edad: Int,
        peso: Double,
        altura: Double?,
        usuarioId: Long
    ) {
        val nuevoPaciente = Pacientes(
            nombre = nombre,
            edad = edad,
            peso = peso,
            altura = altura,
            usuarioId = usuarioId
        )

        // Insertar el nuevo paciente en la base de datos
        insert(nuevoPaciente)
    }
}
