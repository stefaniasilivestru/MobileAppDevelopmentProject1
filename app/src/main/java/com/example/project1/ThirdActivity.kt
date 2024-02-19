package com.example.project1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ThirdActivity  : AppCompatActivity(){

    private val TAG1 = "buttonToSecond"
    private val TAG2 = "activityCreated"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)

        Log.i(TAG2, "The third activity was created")

        val buttonToSecond : Button = findViewById(R.id.buttonThirdActivity)
        buttonToSecond.setOnClickListener(){
            Log.w(TAG1, "Button was clicked and we are going back to the second activity")
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }
    }
}