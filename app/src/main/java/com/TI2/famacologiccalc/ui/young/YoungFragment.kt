package com.TI2.famacologiccalc.ui.young

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.TI2.famacologiccalc.database.AppDatabase
import com.TI2.famacologiccalc.database.repositories.PacienteRepository
import com.TI2.famacologiccalc.database.repositories.RegistroDeUsoRepository
import com.TI2.famacologiccalc.database.repositories.UsuarioRepository
import com.TI2.famacologiccalc.database.session.ActualPatient
import com.TI2.famacologiccalc.database.session.ActualSession
import com.TI2.famacologiccalc.databinding.FragmentYoungBinding
import com.TI2.famacologiccalc.ui.ViewModelFactory

class YoungFragment : Fragment() {

    private var _binding: FragmentYoungBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: YoungViewModel
    private var isEditMode = false // Variable para alternar entre modos

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentYoungBinding.inflate(inflater, container, false)

        // Inicialización de DAOs
        val usuarioDao = AppDatabase.getDatabase(requireContext()).usuarioDao()
        val pacienteDao = AppDatabase.getDatabase(requireContext()).pacienteDao()
        val registroDeUsoDao = AppDatabase.getDatabase(requireContext()).registrosDeUsoDao()

        val usuarioRepository = UsuarioRepository(usuarioDao)
        val pacienteRepository = PacienteRepository(pacienteDao)
        val registroDeUsoRepository = RegistroDeUsoRepository(registroDeUsoDao)

        val viewModelFactory = ViewModelFactory(usuarioRepository, pacienteRepository, registroDeUsoRepository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(YoungViewModel::class.java)

        // Observando el LiveData 'result' para actualizar el UI automáticamente
        viewModel.result.observe(viewLifecycleOwner, { result ->
            binding.tvResult.text = "Resultado: $result"
        })

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
        toggleField(binding.tvStandardWeight, binding.etStandardWeight, editMode)

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
                binding.tvStandardWeight.id -> "Peso estándar: ${et.text}"
                else -> ""
            }
        }
    }

    private fun setupCalculateButton() {
        binding.btnCalculate.setOnClickListener {
            val weightValue = binding.etWeight.text.toString().toDoubleOrNull()
            val standardWeightValue = binding.etStandardWeight.text.toString().toDoubleOrNull()
            val standardDosageValue = binding.etDosage.text.toString().toDoubleOrNull()

            if (weightValue != null && standardWeightValue != null && standardDosageValue != null) {
                val currentUser = ActualSession.usuarioLogeado
                val currentPatient = ActualPatient.pacienteSeleccionado

                if (currentUser != null && currentPatient != null) {
                    // Llamar al ViewModel para calcular y registrar
                    viewModel.calculateAndSaveDosage(
                        userId = currentUser.id.toInt(),
                        patientId = currentPatient.id.toInt(),
                        weight = weightValue,
                        standardWeight = standardWeightValue,
                        standardDosage = standardDosageValue
                    )
                } else {
                    binding.tvResult.text = "No se ha seleccionado un usuario o paciente."
                }
            } else {
                binding.tvResult.text = "Por favor, ingrese valores válidos."
            }
        }
    }

    private fun loadPatientData() {
        ActualPatient.pacienteSeleccionado?.let { paciente ->
            val standardWeight = 70.0 // Definir un valor estándar fijo para el peso (en kg), si no se tiene uno de la base de datos.

            binding.tvPatientName.text = "Paciente: ${paciente.nombre}"
            binding.tvStandardWeight.text = "Peso estándar: $standardWeight kg"  // Mostrar el peso estándar.
            binding.tvWeight.text = "Peso: ${paciente.peso} kg"  // Mostrar el peso del paciente.

            binding.etDosage.setText("") // Dejar espacio en blanco para que ingrese la dosis.
            binding.etWeight.setText(paciente.peso.toString())  // Asignar peso a EditText.
            binding.etStandardWeight.setText(standardWeight.toString())  // Asignar peso estándar al EditText.
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
