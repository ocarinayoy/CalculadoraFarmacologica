package com.TI2.famacologiccalc.ui.sheetdialog.consulta

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.TI2.famacologiccalc.adapters.PacienteAdapter
import com.TI2.famacologiccalc.database.DatabaseInstance
import com.TI2.famacologiccalc.database.repositories.PacienteRepository
import com.TI2.famacologiccalc.databinding.BottomSheetConsultaPacienteBinding
import com.TI2.famacologiccalc.database.session.ActualSession
import com.TI2.famacologiccalc.ui.ViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PacienteConsultaFragment : BottomSheetDialogFragment() {

    private var _binding: BottomSheetConsultaPacienteBinding? = null
    private val binding get() = _binding!!
    private lateinit var bottomSheetViewModel: PacienteConsultaViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomSheetConsultaPacienteBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Inicializar el repositorio y el ViewModel usando el ViewModelFactory
        val pacienteRepository = PacienteRepository(DatabaseInstance.getDatabase(requireContext()).pacienteDao())
        val factory = ViewModelFactory(pacienteRepository = pacienteRepository)
        bottomSheetViewModel = ViewModelProvider(this, factory).get(PacienteConsultaViewModel::class.java)

        // Configurar RecyclerView
        binding.rvPatientList.layoutManager = LinearLayoutManager(requireContext())

        // Obtener el usuario logueado y consultar los pacientes
        val usuarioId = ActualSession.usuarioLogeado?.id
        if (usuarioId != null) {
            bottomSheetViewModel.obtenerPacientesPorUsuario(usuarioId)
        } else {
            Toast.makeText(requireContext(), "Usuario no válido", Toast.LENGTH_SHORT).show()
        }

        // Observar la lista de pacientes
        bottomSheetViewModel.pacientes.observe(viewLifecycleOwner, { pacientes ->
            if (pacientes.isEmpty()) {
                Toast.makeText(requireContext(), "No hay pacientes registrados para este usuario", Toast.LENGTH_SHORT).show()
            } else {
                val adapter = PacienteAdapter(pacientes) { paciente ->
                    // Llamar al ViewModel para seleccionar el paciente
                    bottomSheetViewModel.seleccionarPaciente(paciente)
                }
                binding.rvPatientList.adapter = adapter
            }
        })

        // Configurar el botón de cierre
        binding.btnCloseBottomSheet.setOnClickListener {
            dismiss() // Cerrar el BottomSheet
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
