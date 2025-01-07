package com.TI2.famacologiccalc.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.TI2.famacologiccalc.R
import com.TI2.famacologiccalc.database.DatabaseInstance
import com.TI2.famacologiccalc.databinding.FragmentSettingsBinding
import com.TI2.famacologiccalc.sesion.ActualSession
import com.TI2.famacologiccalc.database.repositories.UsuarioRepository
import com.TI2.famacologiccalc.ui.ViewModelFactory
import com.TI2.famacologiccalc.MainActivity


class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var settingsViewModel: SettingsViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Inicializar la base de datos y el repositorio
        val database = DatabaseInstance.getDatabase(requireContext())
        val usuarioRepository = UsuarioRepository(database.usuarioDao())

        // Usar el ViewModelFactory para crear el SettingsViewModel
        val factory = ViewModelFactory(usuarioRepository,null)
        settingsViewModel = ViewModelProvider(this, factory).get(SettingsViewModel::class.java)

        // Cargar datos del usuario logueado en los EditText
        val usuarioLogeado = ActualSession.usuarioLogeado
        if (usuarioLogeado != null) {
            binding.etName.setText(usuarioLogeado.nombre)
            binding.etEmail.setText(usuarioLogeado.email)
            binding.etSpecialty.setText(usuarioLogeado.especialidad ?: "")
            binding.etPassword.setText(usuarioLogeado.password)
        } else {
            // Manejo del caso donde no hay usuario logueado
            binding.etName.setText("Usuario no disponible")
            binding.etEmail.setText("Correo no disponible")
            binding.etSpecialty.setText("")
        }

        binding.btnSaveChanges.setOnClickListener {
            val username = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val specialty = binding.etSpecialty.text.toString().takeIf { it.isNotBlank() }
            val password = binding.etPassword.text.toString()

            // Validar que el campo de contraseña no esté vacío
            if (password.isBlank()) {
                Toast.makeText(requireContext(), "La contraseña no puede estar vacía", Toast.LENGTH_SHORT).show()
                return@setOnClickListener // Salir de la función si el campo está vacío
            }

            // Actualiza los datos del usuario
            settingsViewModel.updateUserInfo(username, email, specialty ?: "", password)

            // Llama a updateNavHeader en MainActivity
            (activity as? MainActivity)?.updateNavHeader()

            // Muestra un mensaje de confirmación
            Toast.makeText(requireContext(), "Cambios guardados correctamente", Toast.LENGTH_SHORT).show()
        }



        // Botón para cerrar sesión
        binding.btnLogout.setOnClickListener {
            ActualSession.usuarioLogeado = null // Limpiar datos de sesión
            findNavController().navigate(R.id.action_nav_settings_to_nav_login)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}