package com.example.windmoiveapp.model

import java.util.*

data class ChatModel(
    var text: String = "",
    var fromId: String = "",
    var toId: String = "",
    val timestamp: Long = Calendar.getInstance().timeInMillis
) {
}