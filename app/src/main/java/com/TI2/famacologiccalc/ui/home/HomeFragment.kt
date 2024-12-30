package com.TI2.famacologiccalc.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.TI2.famacologiccalc.R
import com.TI2.famacologiccalc.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el diseño para este fragmento
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Vincular las vistas
        val editText = view.findViewById<EditText>(R.id.et_input)
        val button = view.findViewById<Button>(R.id.btn_submit)

        // Configurar el botón
        button.setOnClickListener {
            val inputText = editText.text.toString()
            if (inputText.isNotEmpty()) {
                Toast.makeText(context, "Texto ingresado: $inputText", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Por favor ingresa un texto.", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}