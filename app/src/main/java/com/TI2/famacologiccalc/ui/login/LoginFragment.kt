package com.TI2.famacologiccalc.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.TI2.famacologiccalc.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    // Variable para manejar el ViewBinding (permite acceder a las vistas del layout de forma segura)
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!! // Aseguramos que no es nulo

    // Variable para el ViewModel asociado al fragmento
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inicializamos el ViewModel usando ViewModelProvider
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        // Inflamos el layout usando el ViewBinding
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root // Obtenemos la vista raíz del layout inflado

        // Configuramos el botón de login para que tome los datos de entrada y los envíe al ViewModel
        binding.buttonLogin.setOnClickListener {
            // Extraemos el texto ingresado en los campos de email y contraseña
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()

            // Llamamos a la función de login en el ViewModel
            loginViewModel.login(email, password)
        }

        // Observamos el resultado del login desde el ViewModel
        loginViewModel.loginResult.observe(viewLifecycleOwner) { result ->
            if (result) {
                // Si el resultado es exitoso, mostramos un mensaje de éxito
                Toast.makeText(context, "Login exitoso", Toast.LENGTH_SHORT).show()
            } else {
                // Si el resultado es fallido, mostramos un mensaje de error en el TextView
                binding.textViewError.text = "Credenciales incorrectas"
            }
        }

        return root // Retornamos la vista raíz del layout inflado
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Liberamos el binding cuando la vista se destruye para evitar fugas de memoria
    }
}
