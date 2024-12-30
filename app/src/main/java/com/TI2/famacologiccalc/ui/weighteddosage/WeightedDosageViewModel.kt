package com.TI2.famacologiccalc.ui.weighteddosage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WeightedDosageViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is ponderada diaria Fragment"
    }
    val text: LiveData<String> = _text
}