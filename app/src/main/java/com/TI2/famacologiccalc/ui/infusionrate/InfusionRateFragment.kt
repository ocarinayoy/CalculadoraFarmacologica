package com.TI2.famacologiccalc.ui.infusionrate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.TI2.famacologiccalc.databinding.FragmentInfusionRateBinding

class InfusionRateFragment : Fragment() {

    private var _binding: FragmentInfusionRateBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val InfusionrateViewModel =
            ViewModelProvider(this).get(InfusionRateViewModel::class.java)

        _binding = FragmentInfusionRateBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textInfusionRate
        InfusionrateViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}