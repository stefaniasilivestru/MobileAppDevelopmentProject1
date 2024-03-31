package com.example.project1

import android.app.Application
import android.preference.PreferenceManager
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner


// Handle language change
class LanguageApplication : Application() {
    companion object {
        var selectedLanguage: String? = null
    }

    override fun onCreate() {
        super.onCreate()
        selectedLanguage = PreferenceManager.getDefaultSharedPreferences(this)
            .getString("language", "en")
        updateAppLanguage(selectedLanguage!!)
    }

    fun updateAppLanguage(language: String) {
        selectedLanguage = language
    }

}