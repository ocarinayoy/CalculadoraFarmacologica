package com.TI2.famacologiccalc.ui.formula

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FormulaViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is formula Fragment"
    }
    val text: LiveData<String> = _text
}