package com.TI2.famacologiccalc.ui.clark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.TI2.famacologiccalc.database.AppDatabase
import com.TI2.famacologiccalc.database.repositories.PacienteRepository
import com.TI2.famacologiccalc.database.repositories.RegistroDeUsoRepository
import com.TI2.famacologiccalc.database.repositories.UsuarioRepository
import com.TI2.famacologiccalc.database.session.ActualPatient
import com.TI2.famacologiccalc.database.session.ActualSession
import com.TI2.famacologiccalc.databinding.FragmentClarkBinding
import com.TI2.famacologiccalc.ui.ViewModelFactory

class ClarkFragment : Fragment() {

    private var _binding: FragmentClarkBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ClarkViewModel
    private var isEditMode = false // Variable para alternar entre modos

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentClarkBinding.inflate(inflater, container, false)

        val usuarioDao = AppDatabase.getDatabase(requireContext()).usuarioDao()
        val pacienteDao = AppDatabase.getDatabase(requireContext()).pacienteDao()
        val registroDeUsoDao = AppDatabase.getDatabase(requireContext()).registrosDeUsoDao()

        val usuarioRepository = UsuarioRepository(usuarioDao)
        val pacienteRepository = PacienteRepository(pacienteDao)
        val registroDeUsoRepository = RegistroDeUsoRepository(registroDeUsoDao)

        val viewModelFactory = ViewModelFactory(usuarioRepository, pacienteRepository, registroDeUsoRepository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ClarkViewModel::class.java)

        setupCalculateButton()
        loadPatientData()

        // Configurar el botón de alternar entre editar y guardar
        setupToggleEditMode()

        return binding.root
    }

    private fun setupToggleEditMode() {
        binding.tvEditToggleData.setOnClickListener {
            isEditMode = !isEditMode
            toggleFields(isEditMode)
        }
    }

    private fun toggleFields(editMode: Boolean) {
        toggleField(binding.tvWeight, binding.etWeight, editMode)

        binding.tvEditToggleData.text = if (editMode) "Guardar" else "Editar"
    }

    private fun toggleField(tv: TextView, et: EditText, editMode: Boolean) {
        if (editMode) {
            et.visibility = View.VISIBLE
            tv.visibility = View.GONE
            et.setText(tv.text.toString().replace(Regex("[^0-9.]"), "")) // Limpiar texto no numérico
        } else {
            tv.visibility = View.VISIBLE
            et.visibility = View.GONE
            tv.text = when (tv.id) {
                binding.tvWeight.id -> "Peso: ${et.text}"
                else -> ""
            }
        }
    }

    private fun setupCalculateButton() {
        binding.btnCalculateClark.setOnClickListener {
            val standardDosage = binding.etStandardDosage.text.toString().toDoubleOrNull()

            if (standardDosage != null && standardDosage > 0) {
                val currentUser = ActualSession.usuarioLogeado
                val currentPatient = ActualPatient.pacienteSeleccionado

                if (currentUser != null && currentPatient != null) {
                    // Cálculo de dosis usando la fórmula de Clark
                    val clarkDosage = standardDosage * (currentPatient.peso ?: 0.0) / 70 // Fórmula de Clark

                    // Actualizar el resultado en la UI
                    binding.tvResultClark.text = "Resultado: %.2f mg".format(clarkDosage)

                    // Llamada a ViewModel para guardar el cálculo en la base de datos
                    viewModel.calculateAndSaveDosage(
                        userId = currentUser.id.toInt(),
                        patientId = currentPatient.id.toInt(),
                        dosage = clarkDosage // Pasamos la dosis calculada
                    )
                } else {
                    binding.tvResultClark.text = "No se ha seleccionado un usuario o paciente."
                }
            } else {
                binding.tvResultClark.text = "Por favor, ingrese una dosis estándar válida."
            }
        }
    }


    private fun loadPatientData() {
        ActualPatient.pacienteSeleccionado?.let { paciente ->
            binding.tvPatientName.text = "Paciente: ${paciente.nombre}"
            binding.tvWeight.text = "Peso: ${paciente.peso}"

            binding.etStandardDosage.setText("") // Dejar espacio en blanco para que ingrese la dosis
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
