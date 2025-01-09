package com.TI2.famacologiccalc.ui.fried

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
import com.TI2.famacologiccalc.databinding.FragmentFriedBinding
import com.TI2.famacologiccalc.ui.ViewModelFactory

class FriedFragment : Fragment() {

    private var _binding: FragmentFriedBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: FriedViewModel
    private var isEditMode = false // Variable para alternar entre modos

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFriedBinding.inflate(inflater, container, false)

        val usuarioDao = AppDatabase.getDatabase(requireContext()).usuarioDao()
        val pacienteDao = AppDatabase.getDatabase(requireContext()).pacienteDao()
        val registroDeUsoDao = AppDatabase.getDatabase(requireContext()).registrosDeUsoDao()

        val usuarioRepository = UsuarioRepository(usuarioDao)
        val pacienteRepository = PacienteRepository(pacienteDao)
        val registroDeUsoRepository = RegistroDeUsoRepository(registroDeUsoDao)

        val viewModelFactory = ViewModelFactory(usuarioRepository, pacienteRepository, registroDeUsoRepository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(FriedViewModel::class.java)

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
        toggleField(binding.tvAge, binding.etAge, editMode)
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
                binding.tvAge.id -> "Edad: ${et.text}"
                binding.tvWeight.id -> "Peso: ${et.text}"
                else -> ""
            }
        }
    }

    private fun setupCalculateButton() {
        binding.btnCalculate.setOnClickListener {
            val ageValue = binding.etAge.text.toString().toDoubleOrNull()
            val weightValue = binding.etWeight.text.toString().toDoubleOrNull()
            val standardDosage = binding.etDosage.text.toString().toDoubleOrNull()

            if (ageValue != null && weightValue != null && standardDosage != null) {
                val currentUser = ActualSession.usuarioLogeado
                val currentPatient = ActualPatient.pacienteSeleccionado

                if (currentUser != null && currentPatient != null) {
                    // Llamar al ViewModel para calcular y registrar
                    viewModel.calculateAndSaveDosage(
                        userId = currentUser.id.toInt(),
                        patientId = currentPatient.id.toInt(),
                        age = ageValue,
                        weight = weightValue,
                        standardDosage = standardDosage
                    )

                    binding.tvResult.text = "Resultado: %.2f mg".format(viewModel.result.value?.toDoubleOrNull() ?: 0.0)
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
            binding.tvPatientName.text = "Paciente: ${paciente.nombre}"
            binding.tvAge.text = "Edad: ${paciente.edad}"
            binding.tvWeight.text = "Peso: ${paciente.peso}"

            binding.etDosage.setText("") // Dejar espacio en blanco para que ingrese la dosis
            binding.etWeight.setText(paciente.peso.toString())  // Asignar peso a EditText
            binding.etAge.setText(paciente.altura.toString())  // Asignar altura a EditText
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
