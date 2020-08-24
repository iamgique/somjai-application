package com.digitalacademy.somjai.ui.myPromptQR

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyPromptQRViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is My Prompt QR"
    }
    val text: LiveData<String> = _text
}