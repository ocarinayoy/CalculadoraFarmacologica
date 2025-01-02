package com.TI2.famacologiccalc.ui.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.TI2.famacologiccalc.database.DatabaseInstance
import com.TI2.famacologiccalc.database.repositories.UsuarioRepository
import com.TI2.famacologiccalc.databinding.FragmentLoginBinding
import com.TI2.famacologiccalc.database.models.Usuarios
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect
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

        // Imprimir "Hola Mundo" en la consola
        Log.d("LoginFragment", "Hola Mundo")

        // Insertar un usuario de prueba si la base de datos está vacía
        CoroutineScope(Dispatchers.IO).launch {
            // Comprobar si la base de datos tiene usuarios
            val usuariosExistentes = usuarioRepository.allUsuarios
            usuariosExistentes.collect { usuarios ->
                if (usuarios.isEmpty()) {
                    // Si no hay usuarios, insertar uno de prueba
                    val usuario = Usuarios(id = 1, nombre = "Test", email = "test@prueba.com", password = "1234")
                    usuarioRepository.insert(usuario)
                    Log.d("LoginFragment", "Usuario de prueba insertado")
                }
            }
        }

        // Configurar el botón de login
        binding.buttonLogin.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()

            // Enviar los datos al ViewModel para hacer el login
            loginViewModel.login(email, password)
        }

        // Observar el resultado del login
        loginViewModel.loginResult.observe(viewLifecycleOwner) { result ->
            if (result) {
                // Login exitoso
                Toast.makeText(context, "Login exitoso", Toast.LENGTH_SHORT).show()
            } else {
                // Error en el login
                binding.textViewError.text = "Credenciales incorrectas"
            }
        }

        // Obtener y imprimir la lista de usuarios en consola
        CoroutineScope(Dispatchers.IO).launch {
            usuarioRepository.allUsuarios.collect { usuarios ->
                withContext(Dispatchers.Main) {
                    if (usuarios.isNotEmpty()) {
                        usuarios.forEach { usuario ->
                            Log.d("LoginFragment", "Usuario: ${usuario.email}, Contraseña: ${usuario.password}")
                        }
                    } else {
                        Log.d("LoginFragment", "No hay usuarios en la base de datos.")
                    }
                }
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Liberar el binding
    }
}
