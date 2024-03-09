package com.example.project1.ui.profile

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.project1.R
import com.example.project1.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root


        val textView: TextView = binding.textRegister
        profileViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

//        val username = getUsername()
//        if (username.isNotBlank()) {
//            binding.textRegister.text = "Welcome, $username"
//            binding.buttonRegister.visibility = View.GONE
//            val buttonLogout : Button = binding.buttonLogout
//            buttonLogout.visibility = View.VISIBLE
//        }

        val buttonRegister : Button = binding.buttonRegister
        buttonRegister.setOnClickListener() {
            Toast.makeText(context, "Button clicked", Toast.LENGTH_SHORT).show()
            showLoginDialog()
        }

        val buttonLogout : Button = binding.buttonLogout
        buttonLogout.setOnClickListener() {
            logoutUser()
        }
        return root
    }

    private fun showLoginDialog() {
        val input = EditText(context)
        AlertDialog.Builder(context)
            .setTitle("Enter username")
            .setIcon(R.mipmap.ic_launcher)
            .setView(input)
            .setPositiveButton("Login") { dialog, which ->
                val username = input.text.toString()
                if (username.isNotBlank()) {
                    binding.textRegister.text = "Welcome, $username"
                    // save username to shared preferences
                    saveUsername(username)
                    Log.d("ProfileFragment", "Username saved: $username")
                    Toast.makeText(context, "Welcome, $username", Toast.LENGTH_SHORT).show()
                    binding.buttonRegister.visibility = View.GONE
                    val buttonLogout : Button = binding.buttonLogout
                    buttonLogout.visibility = View.VISIBLE
                } else {
                    Toast.makeText(context, "Please enter a username", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel") { dialog, which ->
                dialog.cancel()
            }
            .show()
    }

    private fun logoutUser() {
        // implement logoutUser -> clear shared preferences

        binding.textRegister.text = "Please login to continue."
        binding.buttonRegister.visibility = View.VISIBLE
        val buttonLogout : Button = binding.buttonLogout
        buttonLogout.visibility = View.GONE
    }

    private fun saveUsername(username: String) {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putString("username", username)
            apply()
        }
    }

    private fun getUsername(): String {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return ""
        return sharedPref.getString("username", "").toString()
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}