package com.example.windmoiveapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.messaging.RemoteMessage

@Entity
data class NotificationModel(
    @PrimaryKey
    val id: String = "",
    val title: String? = "",
    val content: String? = "",
    val img: String? = "",
    val timeStamp: Long? = 0L
)

fun RemoteMessage.convertToNotificationModel(): NotificationModel{
    return NotificationModel(
        id = this.messageId ?: "",
        title = this.notification?.title,
        content = this.notification?.body ?: "",
        img = this.notification?.imageUrl.toString(),
        timeStamp = this.notification?.eventTime
    )
}