package com.example.windmoiveapp.broadcast

import android.app.Application
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.windmoiveapp.extension.showAlertDialog
import com.example.windmoiveapp.util.AppApplication

class DownloadBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == DownloadManager.ACTION_DOWNLOAD_COMPLETE) {
            val downloadId = intent.getLongExtra("downloadID", -1L)
            if (downloadId != -1L) {
                AppApplication.instance.showAlertDialog("Download Video Success")
            } else {
                AppApplication.instance.showAlertDialog("Download Video Failure")
            }
        }
    }

}