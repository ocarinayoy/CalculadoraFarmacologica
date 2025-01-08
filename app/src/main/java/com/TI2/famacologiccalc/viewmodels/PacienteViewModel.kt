package com.TI2.famacologiccalc.viewmodels

import androidx.lifecycle.*
import com.TI2.famacologiccalc.database.models.Pacientes
import com.TI2.famacologiccalc.database.repositories.PacienteRepository
import kotlinx.coroutines.launch

class PacienteViewModel(private val repository: PacienteRepository) : ViewModel() {

    // Obtener todos los pacientes como LiveData
    val allPacientes: LiveData<List<Pacientes>> = repository.allPacientes.asLiveData()

    // Obtener pacientes registrados por un usuario específico
    fun obtenerPacientesPorUsuario(usuarioId: Long): LiveData<List<Pacientes>> {
        return repository.obtenerPacientesPorUsuario(usuarioId).asLiveData()
    }

    // Función que expone los pacientes como LiveData
    fun obtenerPacientes(): LiveData<List<Pacientes>> {
        return allPacientes
    }

    // Insertar un nuevo paciente
    fun insert(paciente: Pacientes) = viewModelScope.launch {
        repository.insert(paciente)
    }

    // Eliminar un paciente
    fun delete(paciente: Pacientes) = viewModelScope.launch {
        repository.delete(paciente)
    }

    // Actualizar un paciente
    fun update(paciente: Pacientes) = viewModelScope.launch {
        repository.update(paciente)
    }

    // Registrar un paciente con todos los campos necesarios
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
}
