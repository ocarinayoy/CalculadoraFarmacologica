package com.TI2.famacologiccalc.ui.young

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.TI2.famacologiccalc.databinding.FragmentYoungBinding

class YoungFragment : Fragment() {

    private var _binding: FragmentYoungBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val YoungViewModel =
            ViewModelProvider(this).get(YoungViewModel::class.java)

        _binding = FragmentYoungBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textYoung
        YoungViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}