package com.TI2.famacologiccalc.ui.weighteddosage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.TI2.famacologiccalc.database.AppDatabase
import com.TI2.famacologiccalc.database.repositories.PacienteRepository
import com.TI2.famacologiccalc.database.repositories.RegistroDeUsoRepository
import com.TI2.famacologiccalc.database.repositories.UsuarioRepository
import com.TI2.famacologiccalc.database.session.ActualPatient
import com.TI2.famacologiccalc.database.session.ActualSession
import com.TI2.famacologiccalc.databinding.FragmentWeighteddosageBinding
import com.TI2.famacologiccalc.ui.ViewModelFactory
import com.TI2.famacologiccalc.viewmodels.PacienteViewModel

class WeightedDosageFragment : Fragment() {

    private var _binding: FragmentWeighteddosageBinding? = null
    private val binding get() = _binding!!

    private var isEditMode = false

    // ViewModel compartido
    private lateinit var viewModel: WeightedDosageViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeighteddosageBinding.inflate(inflater, container, false)

        // Inicializa los DAOs de la base de datos
        val usuarioDao = AppDatabase.getDatabase(requireContext()).usuarioDao()
        val pacienteDao = AppDatabase.getDatabase(requireContext()).pacienteDao()
        val registroDeUsoDao = AppDatabase.getDatabase(requireContext()).registrosDeUsoDao()

// Inicializa los repositorios
        val usuarioRepository = UsuarioRepository(usuarioDao)
        val pacienteRepository = PacienteRepository(pacienteDao)
        val registroDeUsoRepository = RegistroDeUsoRepository(registroDeUsoDao)

// Crear el ViewModelFactory con los repositorios
        val viewModelFactory = ViewModelFactory(usuarioRepository, pacienteRepository, registroDeUsoRepository)

// Crear el ViewModel usando el ViewModelFactory
        viewModel = ViewModelProvider(this, viewModelFactory).get(WeightedDosageViewModel::class.java)

        // Observar cambios en el resultado
        viewModel.result.observe(viewLifecycleOwner, { result ->
            binding.tvResult.text = result
        })

        setupToggleEditMode()
        setupCalculateButton()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Forzar el estado inicial con campos en modo no edición
        isEditMode = false
        toggleFields(isEditMode)

        // Limpiar los valores de los campos
        binding.etWeight.setText("")
        binding.etDosage.setText("")
        binding.etFrequency.setText("")
        binding.tvResult.text = "Resultado: "

        // Cargar los datos del paciente seleccionado
        loadPatientData()
    }

    private fun loadPatientData() {
        ActualPatient.pacienteSeleccionado?.let { paciente ->
            // Cargar los datos en los EditText
            binding.etWeight.setText(paciente.peso?.toString() ?: "")
            // Cargar el nombre del paciente en el TextView
            binding.tvPatientName.text = "Paciente: ${paciente.nombre}"
        }
    }

    private fun setupToggleEditMode() {
        binding.tvEditToggleData.setOnClickListener {
            isEditMode = !isEditMode
            toggleFields(isEditMode)
        }
    }

    private fun toggleFields(editMode: Boolean) {
        toggleField(binding.tvWeight, binding.etWeight, editMode)
        toggleField(binding.tvDosage, binding.etDosage, editMode)
        toggleField(binding.tvFrequency, binding.etFrequency, editMode)

        binding.tvEditToggleData.text = if (editMode) "Guardar" else "Editar"
    }

    private fun toggleField(tv: TextView, et: EditText, editMode: Boolean) {
        if (editMode) {
            et.visibility = View.VISIBLE
            tv.visibility = View.GONE
            et.setText(tv.text.toString().replace(Regex("[^0-9.]"), ""))
        } else {
            tv.visibility = View.VISIBLE
            et.visibility = View.GONE
            tv.text = when (tv.id) {
                binding.tvWeight.id -> "Peso: ${et.text}"
                binding.tvDosage.id -> "Dosificación: ${et.text}"
                binding.tvFrequency.id -> "Frecuencia diaria: ${et.text}"
                else -> ""
            }
        }
    }

    private fun setupCalculateButton() {
        binding.btnCalculate.setOnClickListener {
            val weightValue = binding.etWeight.text.toString().toDoubleOrNull() ?: 0.0
            val dosageValue = binding.etDosage.text.toString().toDoubleOrNull() ?: 0.0
            val frequencyValue = binding.etFrequency.text.toString().toIntOrNull() ?: 0

            if (weightValue > 0 && dosageValue > 0 && frequencyValue > 0) {
                val currentUser = ActualSession.usuarioLogeado
                val currentPatient = ActualPatient.pacienteSeleccionado

                if (currentUser != null && currentPatient != null) {
                    viewModel.weight.value = weightValue.toString()
                    viewModel.dosagePerKg.value = dosageValue.toString()
                    viewModel.frequency.value = frequencyValue.toString()

                    viewModel.calculateAndSaveDosage(
                        userId = currentUser.id.toInt(),
                        patientId = currentPatient.id.toInt()
                    )
                } else {
                    binding.tvResult.text = "No se ha seleccionado un usuario o paciente."
                }
            } else {
                binding.tvResult.text = "Por favor, ingrese valores válidos."
            }
        }
    }


    override fun onResume() {
        super.onResume()

        // Restablecer el estado de edición y limpiar los campos si es necesario
        if (!isEditMode) {
            toggleFields(isEditMode) // Alterna a modo de visualización
        }

        // Limpiar los campos al regresar
        binding.etWeight.setText("")
        binding.etDosage.setText("")
        binding.etFrequency.setText("")
        binding.tvResult.text = "Resultado: "
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
