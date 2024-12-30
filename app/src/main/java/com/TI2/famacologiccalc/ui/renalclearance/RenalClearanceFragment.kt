package com.TI2.famacologiccalc.ui.renalclearance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.TI2.famacologiccalc.databinding.FragmentRenalClearanceBinding

class RenalClearanceFragment : Fragment() {

    private var _binding: FragmentRenalClearanceBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val RenalclearanceViewModel =
            ViewModelProvider(this).get(RenalClearanceViewModel::class.java)

        _binding = FragmentRenalClearanceBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textRenalClearance
        RenalclearanceViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}