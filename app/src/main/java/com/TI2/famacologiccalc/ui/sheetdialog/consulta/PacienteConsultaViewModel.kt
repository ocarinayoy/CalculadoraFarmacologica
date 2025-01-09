package com.TI2.famacologiccalc.ui.sheetdialog.consulta

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.TI2.famacologiccalc.database.models.Pacientes
import com.TI2.famacologiccalc.database.repositories.PacienteRepository
import kotlinx.coroutines.launch

class PacienteConsultaViewModel(
    private val pacienteRepository: PacienteRepository
) : ViewModel() {

    private val _pacientes = MutableLiveData<List<Pacientes>>()
    val pacientes: LiveData<List<Pacientes>> get() = _pacientes

    private val _selectedPaciente = MutableLiveData<Pacientes?>()
    val selectedPaciente: LiveData<Pacientes?> get() = _selectedPaciente

    // Método para obtener pacientes por usuario
    fun obtenerPacientesPorUsuario(usuarioId: Long) {
        viewModelScope.launch {
            // Recoger el Flow y actualizar el LiveData
            pacienteRepository.obtenerPacientesPorUsuario(usuarioId).collect { pacientesList ->
                _pacientes.value = pacientesList
            }
        }
    }

    // Método para seleccionar un paciente
    fun seleccionarPaciente(paciente: Pacientes) {
        _selectedPaciente.value = paciente
    }
}
