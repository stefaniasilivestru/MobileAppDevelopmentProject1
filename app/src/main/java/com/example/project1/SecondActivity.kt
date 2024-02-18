package com.example.project1

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SecondActivity : AppCompatActivity() {
    private val TAG1 = "buttonToMain"
    private val TAG2 = "buttonToThird"
    private val TAG3 = "location"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val bundle = intent.getBundleExtra("locationBundle")
        val location: Location? = bundle?.getParcelable("location")

        if (location != null) {
            Log.i(TAG3, "onCreate: Location["+location.latitude+"]["+location.longitude+"][")
        };

        val buttonToMain : Button = findViewById(R.id.buttonToMain)
        buttonToMain.setOnClickListener {
            Log.d(TAG1, "Button was clicked and we are going back to the main activity")
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val buttonToThird : Button = findViewById(R.id.buttonToThird)
        buttonToThird.setOnClickListener {
            Log.d(TAG2, "Button was clicked and we are going to the third activity")
            val intent = Intent(this, ThirdActivity::class.java)
            startActivity(intent)
        }
    }
}