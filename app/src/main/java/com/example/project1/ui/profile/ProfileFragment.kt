package com.example.project1.ui.profile

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
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {

    private var editEmail : EditText? = null
    private var editPassword : EditText? = null
    private var mAuth : FirebaseAuth? = null
    private var buttonLogin : Button? = null
    private var buttonLogot: Button? = null
    private var buttonRegister: Button? = null
    private var buttonLoginGmail: Button? = null

    companion object {
        private const val RC_SIGN_IN = 123
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        var view : View = inflater.inflate(R.layout.fragment_profile, container, false)
        val buttonToHome : Button = view.findViewById(R.id.button_to_home)

        buttonToHome.setOnClickListener() {
            Toast.makeText(context, "Go back home", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_profile_to_home)
        }

        if (FirebaseAuth.getInstance().currentUser == null) {
            editEmail = view.findViewById(R.id.edit_text_email)
            editPassword = view.findViewById(R.id.edit_text_password)
            buttonLogin = view.findViewById(R.id.button_login)
            buttonRegister = view.findViewById(R.id.button_register)
            buttonLoginGmail = view.findViewById(R.id.button_login_gmail)
            mAuth = FirebaseAuth.getInstance()

            buttonRegister!!.setOnClickListener() {
                Toast.makeText(context, "Go to register", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_profile_to_register)
            }

            buttonLogin!!.setOnClickListener() {
                loginUser()
            }

            buttonLoginGmail!!.setOnClickListener() {
                startGmailSignIn()
            }

        } else {
            view = inflater.inflate(R.layout.fragment_login_connected, container, false)
            buttonLogot = view.findViewById(R.id.button_logout)

            buttonLogot!!.setOnClickListener() {
                logoutUser()
            }
        }

        return view
    }

    private fun logoutUser() {
        Toast.makeText(context, "Logging out", Toast.LENGTH_SHORT).show()
        FirebaseAuth.getInstance().signOut()
        findNavController().navigate(R.id.action_profile_to_home)
    }

    private fun loginUser() {
        val email = editEmail?.text.toString()
        val password = editPassword?.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, "Email and password cannot be empty", Toast.LENGTH_SHORT).show()
        } else {
            mAuth?.signInWithEmailAndPassword(email, password)?.addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                    // go to the home fragment
                    findNavController().navigate(R.id.action_profile_to_home)
                } else {
                    Log.d("Login failed", "Error when logging in")
                    Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun startGmailSignIn() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build() // Add Google authentication
        )

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN
        )
    }
}