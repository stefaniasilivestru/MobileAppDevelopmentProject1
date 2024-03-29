package com.example.project1.ui.register

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.project1.R
import com.google.firebase.auth.FirebaseAuth

class RegisterFragment : Fragment() {
    private var editCreateUser : EditText? = null
    private var editCreatePassword : EditText? = null
    private var editConfirmPassword : EditText? = null
    private var mAuth : FirebaseAuth? = null
    private var buttonRegister : Button? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mAuth = FirebaseAuth.getInstance()
        val view : View = inflater.inflate(R.layout.fragment_register, container, false)
        editCreateUser = view.findViewById(R.id.edit_text_email_register)
        editCreatePassword = view.findViewById(R.id.edit_text_password_register)
        editConfirmPassword = view.findViewById(R.id.edit_text_confirm_password)
        buttonRegister = view.findViewById(R.id.button_register)

        buttonRegister!!.setOnClickListener() {
            registerUser()

        }
        return view;
    }

    private fun registerUser() {
        val email = editCreateUser?.text.toString()
        val password = editCreatePassword?.text.toString()
        val confirmPassword = editConfirmPassword?.text.toString()
        if (email.isEmpty()) {
            editCreateUser?.error = "Please enter email"
            editCreateUser?.requestFocus()
            return
        }
        if (password.isEmpty()) {
            editCreatePassword?.error = "Please enter password"
            editCreatePassword?.requestFocus()
            return
        }
        if (confirmPassword.isEmpty()) {
            editConfirmPassword?.error = "Please confirm password"
            editConfirmPassword?.requestFocus()
            return
        }
        if (password != confirmPassword) {
            editConfirmPassword?.error = "Passwords do not match"
            editConfirmPassword?.requestFocus()
            return
        }
        mAuth!!.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "User created", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_register_to_login)
                } else {
                    Log.d("Register", "Failed to create user")
                    Toast.makeText(context, "Failed to create user", Toast.LENGTH_SHORT).show()

                }
            }
    }
}