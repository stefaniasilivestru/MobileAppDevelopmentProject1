package com.example.project1.ui.home

import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.project1.R
import com.example.project1.databinding.FragmentHomeBinding

class HomeFragment : Fragment(), LocationListener {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textRegister
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        val buttonRegister : Button = binding.buttonRegister
        buttonRegister.setOnClickListener() {
            Toast.makeText(context, "Button clicked", Toast.LENGTH_SHORT).show()
            showLoginDialog()
        }

        val buttonLogout : Button = binding.buttonLogout
        buttonLogout.setOnClickListener() {
            logoutUser()
        }

        locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                locationPermissionCode
            )
        } else {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                5000,
                5f,
                this
            )
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
                    Toast .makeText(context, "Welcome, $username", Toast.LENGTH_SHORT).show()
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



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
            }
        }
    }
}
    override fun onLocationChanged(location: Location) {
        val textView: TextView = binding.textLocation
        textView.text = "Latitude: ${location.latitude}, Longitude: ${location.longitude}"
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
    override fun onProviderEnabled(provider: String) {}
    override fun onProviderDisabled(provider: String) {}

}