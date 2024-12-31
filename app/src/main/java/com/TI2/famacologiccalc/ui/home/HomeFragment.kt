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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.TI2.famacologiccalc.R
import com.TI2.famacologiccalc.adapters.NewsAdapter
import com.TI2.famacologiccalc.databinding.FragmentHomeBinding
import com.TI2.famacologiccalc.database.models.News

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

        // Inicializar RecyclerView
        val recyclerView: RecyclerView = view.findViewById(R.id.rv_news)

        // Lista de noticias
        val newsList = listOf(
            News(
                title = "Nueva actualización de la app",
                description = "Descubre las últimas funcionalidades añadidas.",
                imageResId = R.drawable.ic_menu_update
            ),
            News(
                title = "Tips de enfermería",
                description = "Consejos para mejorar tu práctica diaria.",
                imageResId = R.drawable.ic_menu_tips
            ),
            News(
                title = "Noticias de farmacología",
                description = "Avances recientes en medicamentos y terapias.",
                imageResId = R.drawable.ic_menu_farmacology
            ),
            News(
                title = "Jornada internacional de enfermería",
                description = "Participa en la jornada virtual con expertos de todo el mundo.",
                imageResId = R.drawable.ic_menu_event
            ),
            News(
                title = "Medicación segura en pediatría",
                description = "Guía actualizada sobre el cálculo de dosis en niños.",
                imageResId = R.drawable.ic_menu_pediatrics
            ),
            News(
                title = "Innovaciones en terapias oncológicas",
                description = "Nuevas opciones de tratamiento con menos efectos secundarios.",
                imageResId = R.drawable.ic_menu_oncology
            ),
            News(
                title = "Protocolos actualizados de RCP",
                description = "Descubre los últimos cambios en los protocolos de reanimación.",
                imageResId = R.drawable.ic_menu_rcp
            ),
            News(
                title = "Vacunas en el embarazo",
                description = "Recomendaciones clave para la inmunización durante la gestación.",
                imageResId = R.drawable.ic_menu_farmacology
            ),
            News(
                title = "Uso adecuado de antibióticos",
                description = "Cómo prevenir la resistencia bacteriana en tu práctica clínica.",
                imageResId = R.drawable.ic_menu_farmacology
            ),
            News(
                title = "Congreso anual de farmacología",
                description = "Reserva tu lugar en el evento más importante del año.",
                imageResId = R.drawable.ic_menu_congress
            )
        )

        // Configurar adaptador
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = NewsAdapter(newsList)

        return view
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}