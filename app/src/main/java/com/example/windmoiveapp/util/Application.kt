package com.example.windmoiveapp.util

import android.app.Application
import com.example.windmoiveapp.database.AppDatabase

class AppApplication : Application() {
    val appDatabase by lazy { AppDatabase.getDatabase(this) }

    companion object {
        lateinit var instance: AppApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

}