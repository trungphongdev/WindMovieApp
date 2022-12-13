package com.example.windmoiveapp.model

import com.example.windmoiveapp.constant.ProfileItemType

data class ProfileItemModel(
    val id: Int,
    val src: Int,
    val name: String,
    val type: ProfileItemType
)