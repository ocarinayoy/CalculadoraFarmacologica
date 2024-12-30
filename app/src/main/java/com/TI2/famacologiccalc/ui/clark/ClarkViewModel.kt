package com.TI2.famacologiccalc.ui.clark

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ClarkViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Clark Fragment"
    }
    val text: LiveData<String> = _text
}