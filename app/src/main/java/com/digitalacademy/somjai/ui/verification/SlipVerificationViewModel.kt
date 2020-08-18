package com.digitalacademy.somjai.ui.verification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SlipVerificationViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is Slip Verification"
    }
    val text: LiveData<String> = _text
}