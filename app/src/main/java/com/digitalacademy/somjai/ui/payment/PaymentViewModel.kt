package com.digitalacademy.somjai.ui.payment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PaymentViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is Payment (QR30) Fragment"
    }
    val text: LiveData<String> = _text

}