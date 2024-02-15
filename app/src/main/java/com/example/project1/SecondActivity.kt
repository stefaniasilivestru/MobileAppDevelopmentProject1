package com.example.project1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val button : Button = findViewById(R.id.buttonSecond)
        button.setOnClickListener {
            Log.d("button", "Button was clicked and we are going back to the main activity")
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        Log.w("SecondActivity", "The second activity was created")

        val button3 : Button = findViewById(R.id.buttonThird)
        button3.setOnClickListener {
            Log.d("button", "Button was clicked and we are going to the third activity")
            val intent = Intent(this, ThirdActivity::class.java)
            startActivity(intent)
        }
    }
}