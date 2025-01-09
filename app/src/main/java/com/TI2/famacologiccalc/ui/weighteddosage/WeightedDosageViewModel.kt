package com.TI2.famacologiccalc.ui.weighteddosage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.TI2.famacologiccalc.database.models.RegistroDeUso
import com.TI2.famacologiccalc.database.repositories.PacienteRepository
import com.TI2.famacologiccalc.database.repositories.UsuarioRepository
import com.TI2.famacologiccalc.database.repositories.RegistroDeUsoRepository
import kotlinx.coroutines.launch

class WeightedDosageViewModel(
    private val usuarioRepository: UsuarioRepository,
    private val pacienteRepository: PacienteRepository,
    private val registroDeUsoRepository: RegistroDeUsoRepository
) : ViewModel() {

    // Variables para peso, dosificación y frecuencia (ya existentes)
    val weight = MutableLiveData<String>()
    val dosagePerKg = MutableLiveData<String>()
    val frequency = MutableLiveData<String>()

    private val _result = MutableLiveData<String>()
    val result: LiveData<String> get() = _result

    // Método para calcular y guardar la dosificación
    fun calculateAndSaveDosage(userId: Int, patientId: Int) {
        val weightValue = weight.value?.toDoubleOrNull() ?: 0.0
        val dosagePerKgValue = dosagePerKg.value?.toDoubleOrNull() ?: 0.0
        val frequencyValue = frequency.value?.toIntOrNull() ?: 0

        if (weightValue > 0 && dosagePerKgValue > 0 && frequencyValue > 0) {
            val totalDosage = weightValue * dosagePerKgValue / frequencyValue
            _result.value = "Dosificación diaria total: $totalDosage mg"

            // Crear un nuevo registro de uso
            val registro = RegistroDeUso(
                usuarioId = userId.toLong(),
                pacienteId = patientId.toLong(),
                formula = "Peso x Dosis por kg / Frecuencia",
                resultado = totalDosage
            )

            // Guardar el registro en la base de datos
            viewModelScope.launch {
                registroDeUsoRepository.insertRegistro(registro)
            }
        } else {
            _result.value = "Por favor, ingrese valores válidos."
        }
    }
}