package com.TI2.famacologiccalc.ui.fried

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.TI2.famacologiccalc.database.models.RegistroDeUso
import com.TI2.famacologiccalc.database.repositories.PacienteRepository
import com.TI2.famacologiccalc.database.repositories.RegistroDeUsoRepository
import com.TI2.famacologiccalc.database.repositories.UsuarioRepository
import kotlinx.coroutines.launch

class FriedViewModel(
    private val usuarioRepository: UsuarioRepository,
    private val pacienteRepository: PacienteRepository,
    private val registroDeUsoRepository: RegistroDeUsoRepository
) : ViewModel() {

    val age = MutableLiveData<String>()
    val weight = MutableLiveData<String>()
    val standardDosage = MutableLiveData<String>()

    private val _result = MutableLiveData<String>()
    val result: LiveData<String> get() = _result

    fun calculateAndSaveDosage(userId: Int, patientId: Int, age: Double, weight: Double, standardDosage: Double) {
        if (age > 0 && weight > 0 && standardDosage > 0) {
            // Fórmula de Fried: (Edad del niño / 12) * Dosis estándar
            val totalDosage = (age / 12) * standardDosage
            _result.value = "Dosificación total: $totalDosage mg"

            val registro = RegistroDeUso(
                usuarioId = userId.toLong(),
                pacienteId = patientId.toLong(),
                formula = "Fórmula de Fried",
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
