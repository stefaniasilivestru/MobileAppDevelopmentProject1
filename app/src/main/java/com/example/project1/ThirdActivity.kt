package com.example.project1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ThirdActivity  : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)

        Log.i("ThirdActivity", "The third activity was created")

        val button4 : Button = findViewById(R.id.buttonFourth)
        button4.setOnClickListener(){
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }
    }
}