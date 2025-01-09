package com.TI2.famacologiccalc.ui.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.TI2.famacologiccalc.MainActivity
import com.TI2.famacologiccalc.R
import com.TI2.famacologiccalc.database.DatabaseInstance
import com.TI2.famacologiccalc.database.repositories.UsuarioRepository
import com.TI2.famacologiccalc.databinding.FragmentLoginBinding
import com.TI2.famacologiccalc.database.session.ActualSession
import com.TI2.famacologiccalc.ui.ViewModelFactory


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Inicializar la base de datos y el repositorio
        val database = DatabaseInstance.getDatabase(requireContext())
        val usuarioRepository = UsuarioRepository(database.usuarioDao())

        // Crear el ViewModel con la fábrica
        val factory = ViewModelFactory(usuarioRepository,null)
        loginViewModel = ViewModelProvider(this, factory).get(LoginViewModel::class.java)

        // Configurar botón de login
        binding.buttonLogin.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()

            // Enviar datos al ViewModel
            loginViewModel.login(email, password)
        }


        // Observar el resultado del login
        loginViewModel.usuarioLogueado.observe(viewLifecycleOwner) { usuario ->
            if (usuario != null) {
                Log.d("LoginFragment", "Usuario logeado: ${usuario.nombre}, Email: ${usuario.email}")

                // Guardar el usuario en la sesión actual
                ActualSession.usuarioLogeado = usuario
                ActualSession.isLoggedIn = true

                // Actualizar el header de la MainActivity (asegúrate de que esté disponible)
                (activity as? MainActivity)?.updateNavHeader()

                // Navegar al HomeFragment después del login exitoso
                findNavController().navigate(R.id.action_nav_login_to_nav_home)
            } else {
                // Error en el login
                binding.textViewError.text = "Credenciales incorrectas"
            }
        }

        // Detectar clic en el enlace de "Sign Up"
        binding.textViewSignUp.setOnClickListener {
            // Navegar al fragmento de registro
            findNavController().navigate(R.id.action_nav_login_to_nav_register)
        }

        return root
    }

    override fun onResume() {
        super.onResume()
        // Ocultar el FAB en este fragmento
        (activity as MainActivity).setFabVisibility(false)
    }

    override fun onPause() {
        super.onPause()
        // Restaurar el FAB cuando este fragmento ya no esté visible
        (activity as MainActivity).setFabVisibility(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Liberar el binding
    }
}
