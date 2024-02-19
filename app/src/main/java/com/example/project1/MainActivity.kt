package com.example.project1

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.view.marginRight

class MainActivity : AppCompatActivity(), LocationListener {
    private val TAG1 = "buttonMain"
    private val TAG2 = "activityCreated"
    private val TAG3 = "location"

    private lateinit var locationManager : LocationManager
    private lateinit var latestLocation: Location
    private val locationPermissionCode = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG2, "onCreate: The main activity is being created.");

        val button: Button = findViewById(R.id.buttonMain)
        button.setOnClickListener {
            Log.d(TAG1, "Button from Main Activity was clicked")
            val intent = Intent(this, SecondActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable("location", latestLocation)
            intent.putExtra("locationBundle", bundle)
            startActivity(intent)
        }

        val buttonToMap : Button = findViewById(R.id.buttonToMap)
        buttonToMap.setOnClickListener {
            if (latestLocation != null) {
                val intent = Intent(this, OpenStreetMapActivity::class.java)
                val bundle = Bundle()
                bundle.putParcelable("location", latestLocation)
                intent.putExtra("locationBundle", bundle)
                startActivity(intent)
            } else{
                Log.e(TAG3, "Location not set yet.")
            }
        }

        // Get the location
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                locationPermissionCode
            )
        } else {
            // The location is updated every 5000 milliseconds (or 5 seconds) and/or if the device moves more than 5 meters,
            // whichever happens first
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
                }
            }
        }
    }

    override fun onLocationChanged(location: Location) {
        val textView : TextView = findViewById(R.id.mainTextView)
        textView.text = "Your current location is set at:\nLatitude: ${location.latitude}, Longitude: ${location.longitude}"
        textView.gravity = Gravity.CENTER
        Log.d("location", "Latitude: ${location.latitude}, Longitude: ${location.longitude}")
        latestLocation = location // Initialize latestLocation
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
    override fun onProviderEnabled(provider: String) {}
    override fun onProviderDisabled(provider: String) {}
}