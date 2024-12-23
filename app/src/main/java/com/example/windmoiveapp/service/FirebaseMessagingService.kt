package com.example.windmoiveapp.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.windmoiveapp.R
import com.example.windmoiveapp.database.BuildDaoDatabase
import com.example.windmoiveapp.model.convertToNotificationModel
import com.example.windmoiveapp.ui.MainActivity
import com.example.windmoiveapp.util.AppApplication
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.*
import okhttp3.Dispatcher
import timber.log.Timber

class FirebaseMessageService : FirebaseMessagingService() {
    companion object {
        private const val CHANNEL_ID = "channelID"
        private const val CHANNEL_NAME = "channelName"
        const val MESSAGE = "message"
        private const val NOTIFICATION_ID = 0
        private const val FCM_TAG = "NotificationFCM"

    }


    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Timber.tag(FCM_TAG).d("From: %s", message.from)
        if (message.notification != null) {
            showNotification(message)
            CoroutineScope(Dispatchers.IO).launch {
                BuildDaoDatabase.getNotificationDao(application = AppApplication.instance)
                    .insertNotification(message.convertToNotificationModel())
            }
        }
    }

    private val notificationManager =
        AppApplication.instance.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private val uri: Uri = RingtoneManager.getDefaultUri(
        RingtoneManager.TYPE_NOTIFICATION
    )

    private fun showNotification(remoteMessage: RemoteMessage) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        createNotificationChanel()
        val notificationBuilder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setContentTitle(applicationContext.getString(R.string.app_name))
            .setSmallIcon(R.drawable.logohome)
            .setSound(uri)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_ALL)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setContentText(remoteMessage.notification?.body)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(remoteMessage.notification?.body)
            )
            .setContentIntent(pendingIntent)
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun createNotificationChanel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(
                notificationChannel
            )
        }
    }

}

