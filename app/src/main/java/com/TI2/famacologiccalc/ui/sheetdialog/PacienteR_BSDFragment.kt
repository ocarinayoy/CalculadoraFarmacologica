package com.TI2.famacologiccalc.ui.sheetdialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.TI2.famacologiccalc.database.DatabaseInstance
import com.TI2.famacologiccalc.database.models.Pacientes
import com.TI2.famacologiccalc.database.repositories.PacienteRepository
import com.TI2.famacologiccalc.database.repositories.UsuarioRepository
import com.TI2.famacologiccalc.databinding.BottomSheetPacienteBinding
import com.TI2.famacologiccalc.sesion.ActualSession
import com.TI2.famacologiccalc.ui.ViewModelFactory
import com.TI2.famacologiccalc.viewmodels.PacienteViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PacienteR_BSDFragment : BottomSheetDialogFragment() {

    private var _binding: BottomSheetPacienteBinding? = null
    private val binding get() = _binding!!
    private lateinit var pacienteViewModel: PacienteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomSheetPacienteBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Inicializar la base de datos y el repositorio
        val database = DatabaseInstance.getDatabase(requireContext())
        val pacienteRepository = PacienteRepository(database.pacienteDao())
        val usuarioRepository = UsuarioRepository(DatabaseInstance.getDatabase(requireContext()).usuarioDao())

// Crear el ViewModel con la fábrica, pasando ambos repositorios
        val factory = ViewModelFactory(usuarioRepository, pacienteRepository)
        pacienteViewModel = ViewModelProvider(this, factory).get(PacienteViewModel::class.java)


        // Configuración del botón de registrar paciente
        binding.btnRegistrarPaciente.setOnClickListener {
            val nombre = binding.etNombrePaciente.text.toString()
            val edad = binding.etEdadPaciente.text.toString().toIntOrNull()
            val peso = binding.etPesoPaciente.text.toString().toDoubleOrNull()
            val altura = binding.etAlturaPaciente.text.toString().toDoubleOrNull()

            if (nombre.isNotEmpty() && edad != null && peso != null) {
                // Verificar que el usuario esté logueado y obtener su ID
                val usuarioId = ActualSession.usuarioLogeado?.id
                if (usuarioId != null) {
                    // Crear un nuevo objeto Pacientes
                    val paciente = Pacientes(
                        nombre = nombre,
                        edad = edad,
                        peso = peso,
                        altura = altura,
                        usuarioId = usuarioId // Asociamos al paciente con el usuario logueado
                    )

                    // Registrar el paciente
                    pacienteViewModel.insert(paciente)
                    Toast.makeText(requireContext(), "Paciente registrado exitosamente", Toast.LENGTH_SHORT).show()
                    dismiss() // Cerrar el BottomSheet
                } else {
                    Toast.makeText(requireContext(), "No se pudo obtener el usuario logueado", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Por favor, completa todos los campos correctamente", Toast.LENGTH_SHORT).show()
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
