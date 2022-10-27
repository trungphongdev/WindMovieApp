package com.example.windmoiveapp.network

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.core.content.ContextCompat.getSystemService

object NetWork {
    @SuppressLint("MissingPermission")
    fun Context.isNetWorkAvailable(): Boolean {
        val connMgr = this.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager ?: return false
        connMgr.allNetworks.forEach { network ->
            if (connMgr.getNetworkInfo(network)?.isConnected == true) {
                return true
            }
        }
        return false
    }
}