package com.TI2.famacologiccalc.ui.clark

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.TI2.famacologiccalc.database.models.RegistroDeUso
import com.TI2.famacologiccalc.database.repositories.PacienteRepository
import com.TI2.famacologiccalc.database.repositories.UsuarioRepository
import com.TI2.famacologiccalc.database.repositories.RegistroDeUsoRepository
import kotlinx.coroutines.launch

class ClarkViewModel(
    private val usuarioRepository: UsuarioRepository,
    private val pacienteRepository: PacienteRepository,
    private val registroDeUsoRepository: RegistroDeUsoRepository
) : ViewModel() {

    val weight = MutableLiveData<String>()
    val dosagePerKg = MutableLiveData<String>()

    private val _result = MutableLiveData<String>()
    val result: LiveData<String> get() = _result

    fun calculateAndSaveDosage(userId: Int, patientId: Int, dosage: Double) {
        if (dosage > 0) {
            _result.value = "Dosificación total: $dosage mg" // Mostrar el resultado

            val registro = RegistroDeUso(
                usuarioId = userId.toLong(),
                pacienteId = patientId.toLong(),
                formula = "Fórmula de Clark",
                resultado = dosage
            )

            viewModelScope.launch {
                registroDeUsoRepository.insertRegistro(registro) // Guardamos el registro en la base de datos
            }
        } else {
            _result.value = "Cálculo de dosis inválido."
        }
    }

}
