package com.TI2.famacologiccalc.ui.young

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class YoungViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Young Fragment"
    }
    val text: LiveData<String> = _text
}