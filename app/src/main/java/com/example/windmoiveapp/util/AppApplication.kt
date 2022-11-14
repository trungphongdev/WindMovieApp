package com.example.windmoiveapp.util

import android.app.Application
import android.content.Context
import com.example.windmoiveapp.database.AppDatabase
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.appopen.AppOpenAd

class AppApplication : Application() {
    val appDatabase by lazy { AppDatabase.getDatabase(this) }
    private lateinit var appOpenAdManager: AppOpenAdManager
    companion object {
        lateinit var instance: AppApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        MobileAds.initialize(this) {}
        appOpenAdManager = AppOpenAdManager()
    }

    private inner class AppOpenAdManager {
        private var appOpenAd: AppOpenAd? = null
        private var isLoadingAd = false
        var isShowingAd = false

        /** Request an ad. */
        fun loadAd(context: Context) {
            // We will implement this below.
        }

        /** Check if ad exists and can be shown. */
        private fun isAdAvailable(): Boolean {
            return appOpenAd != null
        }
    }

}