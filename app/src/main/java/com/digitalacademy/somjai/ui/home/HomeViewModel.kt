package com.digitalacademy.somjai.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        /*value = "Welcome to Somjai Application \n" +
                "Please Login"*/
    }
    val text: LiveData<String> = _text
}