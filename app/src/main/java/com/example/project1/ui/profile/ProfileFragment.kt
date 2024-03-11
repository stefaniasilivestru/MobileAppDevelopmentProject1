package com.example.project1.ui.profile

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.project1.MainActivity
import com.example.project1.R
import com.example.project1.UserEntity
import com.example.project1.UserLocationEntity
import com.example.project1.databinding.FragmentProfileBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
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

        // Initialize ViewMode
        // Observing isLoggedIn LiveData
        profileViewModel.isLoggedIn.observe(viewLifecycleOwner) { isLoggedIn ->
            updateButtonVisibility(isLoggedIn)
        }
        profileViewModel.checkLoggedInState(getUsernameFromSharedPref())

        val textView: TextView = binding.textRegister
        profileViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }


        val buttonRegister : Button = binding.buttonRegister
        buttonRegister.setOnClickListener() {
            Toast.makeText(context, "Button clicked", Toast.LENGTH_SHORT).show()
            showRegisterDialog()
        }

        val buttonLogin : Button = binding.buttonLogin
        buttonLogin.setOnClickListener() {
           Toast.makeText(context, "Button clicked", Toast.LENGTH_SHORT).show()
            showLoginDialog()
            Log.d("user from shared", getUsernameFromSharedPref())
        }

        val buttonLogout : Button = binding.buttonLogout
        buttonLogout.setOnClickListener() {
            loggoutUser()
            Log.d("user", "User logged out")

        }

        return root
    }

    // Register function
    private fun showRegisterDialog() {
        val usernameInput = EditText(context)
        val passwordInput = EditText(context)
        val repeatPasswordInput = EditText(context)

        usernameInput.hint = "Username"
        passwordInput.hint = "Password"
        repeatPasswordInput.hint = "Repeat password"


        AlertDialog.Builder(context)
            .setTitle("Register")
            .setIcon(R.mipmap.ic_launcher)
            .setView(
                LinearLayout(context).apply {
                    orientation = LinearLayout.VERTICAL
                    addView(usernameInput)
                    addView(passwordInput)
                    addView(repeatPasswordInput)
                }
            )
            .setPositiveButton("Register") { dialog, which ->
                val username = usernameInput.text.toString()

                lifecycleScope.launch(Dispatchers.IO) {
                    val userDAO = MainActivity.database.userDao()
                    if (username.isNotBlank() && userDAO.getUserByUsername(username) == null) {
                        val password = passwordInput.text.toString()
                        val repeatPassword = repeatPasswordInput.text.toString()
                        // check password match
                        if (password == repeatPassword) {
                            val user = UserEntity(0, username, password)
                            userDAO.insertUser(user)

                            //TODO: insert user in the database using DAO for each user locations EXAMPLE:
                            val userLocation = UserLocationEntity(0, userDAO.getIdByUsername(username) ,"Madrid", "Sol", 40.416775, -3.703790)
                            userDAO.insertUserLocation(userLocation)

                            val userLocation2 = UserLocationEntity(0, userDAO.getIdByUsername(username) ,"Madrid", "Gran Via", 40.420006, -3.709924)
                            userDAO.insertUserLocation(userLocation2)

                            showToast("User registered: $username")
                            Log.d("user", "User registered: $username")
                        } else {
                            showToast("Passwords do not match")
                            Log.d("user", "Passwords do not match")
                        }
                    } else {
                        showToast("User already exists: $username")
                        Log.d("user", "User already exists: $username")
                    }
                }
            }
            .setNegativeButton("Cancel") { dialog, which ->
                dialog.cancel()
            }
            .show()
    }

    // Login function
    private fun showLoginDialog() {
        val usernameInput = EditText(context)
        val passwordInput = EditText(context)

        usernameInput.hint = "Username"
        passwordInput.hint = "Password"

        AlertDialog.Builder(context)
            .setTitle("Login")
            .setIcon(R.mipmap.ic_launcher)
            .setView(
                LinearLayout(context).apply {
                    orientation = LinearLayout.VERTICAL
                    addView(usernameInput)
                    addView(passwordInput)
                }
            )
            .setPositiveButton("Login") { dialog, which ->
                val username = usernameInput.text.toString()
                val password = passwordInput.text.toString()

                lifecycleScope.launch(Dispatchers.IO) {
                    val userDAO = MainActivity.database.userDao()
                    val user = userDAO.getUserByUsername(username)
                    if (user != null && user.password == password) {
                        showToast("User logged in: $username")
                        // save the username in shared preferences as token
                        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return@launch
                        with (sharedPref.edit()) {
                            putString("username", username)
                            apply()
                        }

                        Log.d("user from shared", getUsernameFromSharedPref())
                        Log.d("user", "User logged in: $username")

                        val intent = Intent(context, MainActivity::class.java)
                        startActivity(intent)


                    } else {
                        showToast("Invalid username or password")
                        Log.d("user", "Invalid username or password")
                    }
                }
            }
            .setNegativeButton("Cancel") { dialog, which ->
                dialog.cancel()
            }
            .show()
    }


    fun getUsernameFromSharedPref(): String {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return ""
        Log.d("ProfileFragment", sharedPref.all.toString())
        return sharedPref.getString("username", "").toString()
    }


    private fun showToast(message: String) {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(
                requireContext(),
                message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun updateButtonVisibility(isLoggedIn: Boolean) {
        if (isLoggedIn) {
            binding.buttonRegister.visibility = View.GONE
            binding.buttonLogin.visibility = View.GONE
            binding.buttonLogout.visibility = View.VISIBLE
        } else {
            binding.buttonRegister.visibility = View.VISIBLE
            binding.buttonLogin.visibility = View.VISIBLE
            binding.buttonLogout.visibility = View.GONE
        }
    }

    private fun loggoutUser() {
        Toast.makeText(context, "User logged out", Toast.LENGTH_SHORT).show()
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putString("username", "")
            apply()
        }
        val intent = Intent(context, MainActivity::class.java)
        startActivity(intent)
    }


    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}