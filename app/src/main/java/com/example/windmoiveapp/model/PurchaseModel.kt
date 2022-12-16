package com.example.windmoiveapp.model

import java.util.*

data class PurchaseModel(
    val token: String = UUID.randomUUID().toString(),
    val userModel: UserModel,
    val bankAccNo: String = "1111 2222 333 4444",
    val price: Int,
    val startDate: Long = Calendar.getInstance().timeInMillis,
    val endDate: Long
) {
}