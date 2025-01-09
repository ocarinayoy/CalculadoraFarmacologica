package com.TI2.famacologiccalc.ui.surfacedosage

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
import com.TI2.famacologiccalc.databinding.FragmentSurfaceDosageBinding
import com.TI2.famacologiccalc.ui.ViewModelFactory

class SurfaceDosageFragment : Fragment() {

    private var _binding: FragmentSurfaceDosageBinding? = null
    private val binding get() = _binding!!

    private var isEditMode = false // Variable para alternar entre modos
    private lateinit var viewModel: SurfaceDosageViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSurfaceDosageBinding.inflate(inflater, container, false)

        val usuarioDao = AppDatabase.getDatabase(requireContext()).usuarioDao()
        val pacienteDao = AppDatabase.getDatabase(requireContext()).pacienteDao()
        val registroDeUsoDao = AppDatabase.getDatabase(requireContext()).registrosDeUsoDao()

        val usuarioRepository = UsuarioRepository(usuarioDao)
        val pacienteRepository = PacienteRepository(pacienteDao)
        val registroDeUsoRepository = RegistroDeUsoRepository(registroDeUsoDao)

        val viewModelFactory = ViewModelFactory(usuarioRepository, pacienteRepository, registroDeUsoRepository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(SurfaceDosageViewModel::class.java)

        setupCalculateButton()
        observeViewModel()
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
        toggleField(binding.tvHeight, binding.etHeight, editMode)

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
                binding.tvHeight.id -> "Altura: ${et.text}"
                else -> ""
            }
        }
    }

    private fun setupCalculateButton() {
        binding.btnCalculate.setOnClickListener {
            val weight = binding.etWeight.text.toString().toDoubleOrNull()
            val height = binding.etHeight.text.toString().toDoubleOrNull()
            val dosagePerM2 = binding.etStandardDose.text.toString().toDoubleOrNull()

            if (weight != null && height != null && dosagePerM2 != null && weight > 0 && height > 0) {
                val surfaceArea = Math.sqrt((weight * height) / 3600)

                val currentUser = ActualSession.usuarioLogeado
                val currentPatient = ActualPatient.pacienteSeleccionado

                if (currentUser != null && currentPatient != null) {
                    viewModel.surfaceArea.value = surfaceArea.toString()
                    viewModel.dosagePerM2.value = dosagePerM2.toString()

                    viewModel.calculateAndSaveDosage(
                        userId = currentUser.id.toInt(),
                        patientId = currentPatient.id.toInt()
                    )

                    binding.tvResult.text = "Superficie Corporal: %.2f m²".format(surfaceArea)
                } else {
                    binding.tvResult.text = "No se ha seleccionado un usuario o paciente."
                }
            } else {
                binding.tvResult.text = "Por favor, ingrese valores válidos para peso, altura y dosis."
            }
        }
    }

    private fun observeViewModel() {
        viewModel.result.observe(viewLifecycleOwner) { result ->
            binding.tvResultDose.text = result
        }
    }

    private fun loadPatientData() {
        ActualPatient.pacienteSeleccionado?.let { paciente ->
            // Cargar los valores en los TextView
            binding.tvPatientName.text = "Paciente: ${paciente.nombre}"
            binding.tvWeight.text = "Peso: ${paciente.peso}"
            binding.tvHeight.text = "Altura: ${paciente.altura}"

            // Asignar los valores a las variables correspondientes
            binding.etWeight.setText(paciente.peso.toString())  // Asignar peso a EditText
            binding.etHeight.setText(paciente.altura.toString())  // Asignar altura a EditText
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
