package com.example.windmoiveapp.network

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

object NetWork {
    @SuppressLint("MissingPermission")
    fun Context.isNetWorkAvailable(): Boolean {
        val connMgr = this.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager ?: return false
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                val cap = connMgr.getNetworkCapabilities(connMgr.activeNetwork) ?: return false
                return cap.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            }
            else -> {
                connMgr.allNetworks.forEach { network ->
                    val info = connMgr.getNetworkInfo(network)
                    if (info != null && info.isConnected) {
                        return true
                    }
                }
            }
        }

        return false
    }
}