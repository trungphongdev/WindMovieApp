package com.example.windmoiveapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.windmoiveapp.service.FirebaseMessageService.Companion.MESSAGE
import com.google.firebase.messaging.RemoteMessage

@Entity
class NotificationModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String? = "",
    val content: String? = "",
    val img: String? = "",
    val timeStamp: Long? = 0L
)

fun RemoteMessage.convertToNotificationModel(): NotificationModel{
    return NotificationModel(
        title = this.notification?.title,
        content = this.data[MESSAGE],
        img = this.notification?.imageUrl.toString(),
        timeStamp = this.notification?.eventTime
    )
}