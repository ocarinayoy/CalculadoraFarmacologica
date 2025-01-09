package com.TI2.famacologiccalc.ui.weighteddosage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.TI2.famacologiccalc.database.repositories.PacienteRepository
import com.TI2.famacologiccalc.database.repositories.UsuarioRepository

class WeightedDosageViewModel(
    private val usuarioRepository: UsuarioRepository,
    private val pacienteRepository: PacienteRepository
) : ViewModel() {

    // Variables para peso, dosificación (en mg por kg) y frecuencia
    val weight = MutableLiveData<String>()
    val dosagePerKg = MutableLiveData<String>()
    val frequency = MutableLiveData<String>()

    // Resultado del cálculo
    private val _result = MutableLiveData<String>()
    val result: LiveData<String> get() = _result

    // Método para calcular la dosificación diaria total en mg
    fun calculateDosage() {
        // Convertir las entradas de texto a valores numéricos
        val weightValue = weight.value?.toDoubleOrNull() ?: 0.0
        val dosagePerKgValue = dosagePerKg.value?.toDoubleOrNull() ?: 0.0
        val frequencyValue = frequency.value?.toIntOrNull() ?: 0

        // Validar que todos los valores sean válidos
        if (weightValue > 0 && dosagePerKgValue > 0 && frequencyValue > 0) {
            // Calcular la dosificación diaria total en mg
            val totalDosage = weightValue * dosagePerKgValue / frequencyValue
            _result.value = "Dosificación diaria total: $totalDosage mg"
        } else {
            _result.value = "Por favor, ingrese valores válidos."
        }
    }
}
