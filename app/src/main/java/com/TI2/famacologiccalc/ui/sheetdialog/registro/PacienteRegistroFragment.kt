package com.TI2.famacologiccalc.ui.sheetdialog.registro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.TI2.famacologiccalc.R
import com.TI2.famacologiccalc.database.DatabaseInstance
import com.TI2.famacologiccalc.database.models.Pacientes
import com.TI2.famacologiccalc.database.repositories.PacienteRepository
import com.TI2.famacologiccalc.database.repositories.UsuarioRepository
import com.TI2.famacologiccalc.databinding.BottomSheetRegistroPacienteBinding
import com.TI2.famacologiccalc.database.session.ActualSession
import com.TI2.famacologiccalc.ui.ViewModelFactory
import com.TI2.famacologiccalc.viewmodels.PacienteViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PacienteRegistroFragment : BottomSheetDialogFragment() {

    private var _binding: BottomSheetRegistroPacienteBinding? = null
    private val binding get() = _binding!!
    private lateinit var pacienteViewModel: PacienteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomSheetRegistroPacienteBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Inicializar la base de datos y el repositorio
        val database = DatabaseInstance.getDatabase(requireContext())
        val pacienteRepository = PacienteRepository(database.pacienteDao())
        val usuarioRepository = UsuarioRepository(DatabaseInstance.getDatabase(requireContext()).usuarioDao())

        // Crear el ViewModel con la fábrica, pasando ambos repositorios
        val factory = ViewModelFactory(usuarioRepository, pacienteRepository)
        pacienteViewModel = ViewModelProvider(this, factory).get(PacienteViewModel::class.java)

        // Configurar el Spinner de estatus
        val estatusAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.estatus_options, // Asegúrate de definir esta lista en strings.xml
            android.R.layout.simple_spinner_item
        )
        estatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spEstatusPaciente.adapter = estatusAdapter

        // Configuración del botón de registrar paciente
        binding.btnRegistrarPaciente.setOnClickListener {
            val nombre = binding.etNombrePaciente.text.toString()
            val edad = binding.etEdadPaciente.text.toString().toIntOrNull()
            val peso = binding.etPesoPaciente.text.toString().toDoubleOrNull()
            val altura = binding.etAlturaPaciente.text.toString().toDoubleOrNull()
            val fechaRegistro = binding.etFechaRegistro.text.toString()
            val estatus = binding.spEstatusPaciente.selectedItem.toString()

            // Verificar que los campos no estén vacíos y realizar la validación
            if (nombre.isNotEmpty() && edad != null && peso != null && fechaRegistro.isNotEmpty()) {
                // Verificar que el usuario esté logueado y obtener su ID
                val usuarioId = ActualSession.usuarioLogeado?.id
                if (usuarioId != null) {
                    // Crear un nuevo objeto Pacientes
                    val paciente = Pacientes(
                        nombre = nombre,
                        edad = edad,
                        peso = peso,
                        altura = altura,
                        fechaRegistro = fechaRegistro,
                        estatus = estatus,
                        usuarioId = usuarioId // Asociamos al paciente con el usuario logueado
                    )

                    // Llamar al método del ViewModel para registrar el paciente
                    pacienteViewModel.registrarPaciente(nombre,edad,peso,altura,fechaRegistro,estatus,usuarioId)

                    // Cerrar el BottomSheetDialog y mostrar mensaje de éxito
                    dismiss()
                    Toast.makeText(requireContext(), "Paciente registrado correctamente", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
