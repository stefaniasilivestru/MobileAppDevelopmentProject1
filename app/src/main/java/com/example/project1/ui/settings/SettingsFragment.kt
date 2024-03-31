package com.example.project1.ui.settings

import android.R
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.example.project1.LanguageApplication
import com.example.project1.databinding.FragmentSettingsBinding
import com.example.project1.databinding.FragmentShareBinding
import com.example.project1.ui.share.ShareViewModel
import java.util.Locale

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root


        val languages = resources.getStringArray(com.example.project1.R.array.languages)
        val spinnerLanguage : Spinner = binding.spinnerLanguage

        // Handle selected language
        spinnerLanguage.setSelection(languages.indexOf(LanguageApplication.selectedLanguage))
        spinnerLanguage.adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, languages)

        spinnerLanguage.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) { // Change the app language based on the selected language
                if (languages != null && position >= 0 && position < languages.size) {
                    setAppLanguage(languages[position])
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }

        }




        return root
    }

    fun setAppLanguage(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val config = resources.configuration
        config.setLocale(locale)

        val editor = PreferenceManager.getDefaultSharedPreferences(requireContext()).edit()
        editor.putString("language", language)
        editor.apply()
        LanguageApplication.selectedLanguage = language
        resources.updateConfiguration(config, resources.displayMetrics)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}