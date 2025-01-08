package com.TI2.famacologiccalc.ui.sheetdialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.TI2.famacologiccalc.database.models.Pacientes
import com.TI2.famacologiccalc.viewmodels.PacienteViewModel
import kotlinx.coroutines.launch

class PacienteR_BSDViewModel(private val pacienteViewModel: PacienteViewModel) : ViewModel() {

    // Suponiendo que `usuarioId` se obtiene desde la sesión actual
    // Este método puede recibir el `usuarioId` como parámetro
    fun registrarPaciente(usuarioId: Long, nombre: String, edad: Int, peso: Double, altura: Double?, fechaRegistro: String, estatus: String) {
        val paciente = Pacientes(
            nombre = nombre,
            edad = edad,
            peso = peso,
            altura = altura,
            fechaRegistro = fechaRegistro,
            estatus = estatus,
            usuarioId = usuarioId // Asociar el paciente al usuario logueado
        )

        // Llamar al método insert del ViewModel
        viewModelScope.launch {
            pacienteViewModel.insert(paciente)
        }
    }
}
