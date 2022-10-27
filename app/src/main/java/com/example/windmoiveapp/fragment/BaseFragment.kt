package com.example.windmoiveapp.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.navOptions
import com.example.windmoiveapp.MainActivity
import com.example.windmoiveapp.R

open class BaseFragment() : Fragment() {
    fun Activity.restartApp() {
       // (this as? MainActivity)?.resetSession()
        val newIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        this.startActivity(newIntent)
        finish()
    }

}