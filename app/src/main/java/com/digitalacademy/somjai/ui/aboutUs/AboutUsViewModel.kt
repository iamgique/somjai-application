package com.digitalacademy.somjai.ui.aboutUs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AboutUsViewModel  : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "About Somjai Application. \n" +
                "Somjai application is an application using for demo SCB Open API."
    }
    val text: LiveData<String> = _text
}