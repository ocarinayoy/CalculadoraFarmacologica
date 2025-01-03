package com.TI2.famacologiccalc.ui.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.TI2.famacologiccalc.database.DatabaseInstance
import com.TI2.famacologiccalc.database.repositories.UsuarioRepository
import com.TI2.famacologiccalc.databinding.FragmentLoginBinding
import com.TI2.famacologiccalc.database.models.Usuarios
import com.TI2.famacologiccalc.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
        val factory = LoginViewModelFactory(usuarioRepository)
        loginViewModel = ViewModelProvider(this, factory).get(LoginViewModel::class.java)

        // Configurar botón de login
        binding.buttonLogin.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()

            // Enviar datos al ViewModel
            loginViewModel.login(email, password)
        }

        // Observar el resultado del login
        loginViewModel.loginResult.observe(viewLifecycleOwner) { result ->
            if (result) {
                // Login exitoso
                Toast.makeText(context, "Login exitoso", Toast.LENGTH_SHORT).show()
                Log.d("LoginFragment", "Login exitoso")
            } else {
                // Error en el login
                binding.textViewError.text = "Credenciales incorrectas"
                Log.d("LoginFragment", "Credenciales incorrectas")
            }
        }

        // Aquí podemos borrar la base de datos para reiniciar los datos
        binding.textViewError.setOnClickListener {
            // Borrar la base de datos de forma definitiva
            CoroutineScope(Dispatchers.IO).launch {
                // Borrar la base de datos
                requireContext().deleteDatabase("famacologic_calc_database")
                Log.d("LoginFragment", "Base de datos borrada")

                // Volver a insertar usuarios de prueba después de borrar la base de datos
                val usuario1 = Usuarios(id = 1, nombre = "admin", email = "admin@fama.com", password = "admin123")
                val usuario2 = Usuarios(id = 2, nombre = "testUser", email = "test@user.com", password = "test123")
                usuarioRepository.insert(usuario1)
                usuarioRepository.insert(usuario2)
                Log.d("LoginFragment", "Usuarios de prueba insertados después de borrar la base de datos")

                // Imprimir la lista de usuarios después de la inserción
                usuarioRepository.allUsuarios.collect { usuarios ->
                    withContext(Dispatchers.Main) {
                        if (usuarios.isNotEmpty()) {
                            usuarios.forEach { usuario ->
                                Log.d("LoginFragment", "Usuario: ${usuario.email}, Contraseña: ${usuario.password}")
                            }
                        } else {
                            Log.d("LoginFragment", "No hay usuarios en la base de datos después de reiniciar.")
                        }
                    }
                }
            }
        }

        // Detectar clic en el enlace de "Sign Up"
        binding.textViewSignUp.setOnClickListener {
            // Navegar al fragmento de registro
            findNavController().navigate(R.id.action_nav_login_to_nav_register)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Liberar el binding
    }
}
