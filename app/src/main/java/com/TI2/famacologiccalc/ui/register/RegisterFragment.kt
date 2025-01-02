package com.TI2.famacologiccalc.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.TI2.famacologiccalc.R
import com.TI2.famacologiccalc.database.DatabaseInstance
import com.TI2.famacologiccalc.database.repositories.UsuarioRepository
import com.TI2.famacologiccalc.ui.ViewModelFactory

class RegisterFragment : Fragment() {

    private lateinit var etUsername: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etEspecialidad: EditText
    private lateinit var btnRegister: Button

    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar las vistas
        etUsername = view.findViewById(R.id.etUsername)
        etEmail = view.findViewById(R.id.etEmail)
        etPassword = view.findViewById(R.id.etPassword)
        etEspecialidad = view.findViewById(R.id.etEspecialidad)
        btnRegister = view.findViewById(R.id.btnRegister)

        // Inicializar la base de datos y el repositorio
        val database = DatabaseInstance.getDatabase(requireContext())
        val usuarioRepository = UsuarioRepository(database.usuarioDao())

        // Usar el ViewModelFactory para crear el RegisterViewModel
        val factory = ViewModelFactory(usuarioRepository)
        registerViewModel = ViewModelProvider(this, factory).get(RegisterViewModel::class.java)

        // Observar el resultado del registro
        registerViewModel.registerResult.observe(viewLifecycleOwner, { result ->
            if (result) {
                // Registro exitoso, redirigir al login o pantalla principal
                Toast.makeText(requireContext(), "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            } else {
                // Usuario ya existe
                Toast.makeText(requireContext(), "El usuario ya existe", Toast.LENGTH_SHORT).show()
            }
        })

        // Acción al presionar el botón de registro
        btnRegister.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val especialidad = etEspecialidad.text.toString().trim()

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || especialidad.isEmpty()) {
                Toast.makeText(requireContext(), "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                // Llamar al ViewModel para registrar el usuario
                registerViewModel.register(username, email, password, especialidad)
            }
        }
    }
}
