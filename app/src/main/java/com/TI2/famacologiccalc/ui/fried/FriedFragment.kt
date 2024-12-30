package com.TI2.famacologiccalc.ui.fried

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.TI2.famacologiccalc.databinding.FragmentFriedBinding

class FriedFragment : Fragment() {

    private var _binding: FragmentFriedBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val FriedViewModel =
            ViewModelProvider(this).get(FriedViewModel::class.java)

        _binding = FragmentFriedBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textFried
        FriedViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}