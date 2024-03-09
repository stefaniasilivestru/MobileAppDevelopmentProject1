package com.example.project1.ui.contactus

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ContactusViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = ""
    }

    private val _imageLogo = MutableLiveData<Int>().apply {
        value = com.example.project1.R.drawable.logo_tripify
    }

    val text: LiveData<String> = _text
    val imageLogo: LiveData<Int> = _imageLogo


    fun setText(text: String) {
        _text.value = text
    }
}