package com.example.project1.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SettingsViewModel : ViewModel() {
    private val _textSettings = MutableLiveData<String>().apply {
        value ="settings fragment"
    }
    val text: LiveData<String> = _textSettings
}