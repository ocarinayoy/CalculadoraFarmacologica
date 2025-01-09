package com.TI2.famacologiccalc.ui.surfacedosage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.TI2.famacologiccalc.database.models.RegistroDeUso
import com.TI2.famacologiccalc.database.repositories.PacienteRepository
import com.TI2.famacologiccalc.database.repositories.UsuarioRepository
import com.TI2.famacologiccalc.database.repositories.RegistroDeUsoRepository
import kotlinx.coroutines.launch

class SurfaceDosageViewModel(
    private val usuarioRepository: UsuarioRepository,
    private val pacienteRepository: PacienteRepository,
    private val registroDeUsoRepository: RegistroDeUsoRepository
) : ViewModel() {

    val surfaceArea = MutableLiveData<String>()
    val dosagePerM2 = MutableLiveData<String>()

    private val _result = MutableLiveData<String>()
    val result: LiveData<String> get() = _result

    fun calculateAndSaveDosage(userId: Int, patientId: Int) {
        val surfaceAreaValue = surfaceArea.value?.toDoubleOrNull() ?: 0.0
        val dosagePerM2Value = dosagePerM2.value?.toDoubleOrNull() ?: 0.0

        if (surfaceAreaValue > 0 && dosagePerM2Value > 0) {
            val totalDosage = surfaceAreaValue * dosagePerM2Value
            _result.value = "Dosificación total: $totalDosage mg"

            val registro = RegistroDeUso(
                usuarioId = userId.toLong(),
                pacienteId = patientId.toLong(),
                formula = "Área de superficie x Dosis por m²",
                resultado = totalDosage
            )

            viewModelScope.launch {
                registroDeUsoRepository.insertRegistro(registro)
            }
        } else {
            _result.value = "Por favor, ingrese valores válidos."
        }
    }
}
