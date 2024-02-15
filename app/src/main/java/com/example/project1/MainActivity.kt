package com.example.project1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button: Button = findViewById(R.id.buttonMain)
        button.setOnClickListener {
            Log.d("button", "Button was clicked")
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }

    }
}