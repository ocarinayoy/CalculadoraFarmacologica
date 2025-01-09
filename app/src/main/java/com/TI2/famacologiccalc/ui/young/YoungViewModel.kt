package com.TI2.famacologiccalc.ui.young

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.TI2.famacologiccalc.database.models.RegistroDeUso
import com.TI2.famacologiccalc.database.repositories.PacienteRepository
import com.TI2.famacologiccalc.database.repositories.RegistroDeUsoRepository
import com.TI2.famacologiccalc.database.repositories.UsuarioRepository
import com.TI2.famacologiccalc.database.session.ActualPatient
import com.TI2.famacologiccalc.database.session.ActualSession
import kotlinx.coroutines.launch

class YoungViewModel(
    private val usuarioRepository: UsuarioRepository,
    private val pacienteRepository: PacienteRepository,
    private val registroDeUsoRepository: RegistroDeUsoRepository
) : ViewModel() {

    val weight = MutableLiveData<String>() // Peso del niño
    val standardWeight = MutableLiveData<String>() // Peso estándar del niño
    val standardDosage = MutableLiveData<String>() // Dosis estándar (como para un adulto)

    private val _result = MutableLiveData<String>()
    val result: LiveData<String> get() = _result

    fun calculateAndSaveDosage(userId: Int, patientId: Int, weight: Double, standardWeight: Double, standardDosage: Double) {
        if (weight > 0 && standardWeight > 0 && standardDosage > 0) {
            // Fórmula de Young: (Peso del niño / Peso estándar) * Dosis estándar
            val totalDosage = (weight / standardWeight) * standardDosage
            _result.value = "Dosis calculada: $totalDosage mg"

            val registro = RegistroDeUso(
                usuarioId = userId.toLong(),
                pacienteId = patientId.toLong(),
                formula = "Fórmula de Young",
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
