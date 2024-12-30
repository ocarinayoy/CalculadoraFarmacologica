package com.TI2.famacologiccalc.ui.fried

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FriedViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Fried Fragment"
    }
    val text: LiveData<String> = _text
}