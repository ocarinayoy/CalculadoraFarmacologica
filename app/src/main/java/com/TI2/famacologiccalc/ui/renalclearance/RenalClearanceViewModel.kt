package com.TI2.famacologiccalc.ui.renalclearance

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RenalClearanceViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Renal Clearance Fragment"
    }
    val text: LiveData<String> = _text
}