package com.example.homehelperfinder.ui

import android.content.Intent
import android.os.Bundle
import android.widget.TextView

import androidx.activity.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import com.example.homehelperfinder.R

class PostRequestActivity : AppCompatActivity() {
//    lateinit var services : List<>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_post_request_step1)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}

class PostRequestViewModel (
    val serviceId : Int,
    val AddressId : Int,
    val startTime : String,
    val
)