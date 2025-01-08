package com.TI2.famacologiccalc.viewmodels

import androidx.lifecycle.*
import com.TI2.famacologiccalc.database.models.Pacientes
import com.TI2.famacologiccalc.database.repositories.PacienteRepository
import kotlinx.coroutines.launch

class PacienteViewModel(private val repository: PacienteRepository) : ViewModel() {

    val allPacientes: LiveData<List<Pacientes>> = repository.allPacientes.asLiveData()

    // Función que expone los pacientes como LiveData
    fun obtenerPacientes(): LiveData<List<Pacientes>> {
        return allPacientes
    }

    fun insert(paciente: Pacientes) = viewModelScope.launch {
        repository.insert(paciente)
    }

    fun delete(paciente: Pacientes) = viewModelScope.launch {
        repository.delete(paciente)
    }

    fun update(paciente: Pacientes) = viewModelScope.launch {
        repository.update(paciente)
    }

    // Función para registrar un paciente con los nuevos campos
    fun registrarPaciente(
        nombre: String,
        edad: Int,
        peso: Double?,
        altura: Double?,
        fechaRegistro: String,
        estatus: String,
        usuarioId: Long
    ) {
        val nuevoPaciente = Pacientes(
            nombre = nombre,
            edad = edad,
            peso = peso,
            altura = altura,
            fechaRegistro = fechaRegistro,
            estatus = estatus,
            usuarioId = usuarioId
        )
        insert(nuevoPaciente)
    }

    // Función para obtener el paciente asociado al usuario
    fun obtenerPacientePorUsuario(usuarioId: Long): LiveData<Pacientes?> {
        return repository.obtenerPacientePorUsuario(usuarioId).asLiveData()
    }
}
