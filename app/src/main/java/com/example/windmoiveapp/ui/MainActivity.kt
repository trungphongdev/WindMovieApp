package com.example.windmoiveapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.windmoiveapp.R
import com.example.windmoiveapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}