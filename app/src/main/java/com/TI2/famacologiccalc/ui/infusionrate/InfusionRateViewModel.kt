package com.TI2.famacologiccalc.ui.infusionrate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class InfusionRateViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Infusion Rate Fragment"
    }
    val text: LiveData<String> = _text
}