package com.TI2.famacologiccalc.ui.surfacedosage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SurfaceDosageViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Surface Dosage Fragment"
    }
    val text: LiveData<String> = _text
}