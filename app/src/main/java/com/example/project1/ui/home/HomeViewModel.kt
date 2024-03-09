package com.example.project1.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Welcome to our app. Here you can find the best places to visit in Spain. \n \n Please login in order to continue."
    }
    val text: LiveData<String> = _text
}