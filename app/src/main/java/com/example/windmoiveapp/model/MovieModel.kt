package com.example.windmoiveapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class MovieModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String? = "",
    val trailerUrl: String? = "",
    val movieUrl: String? = "",
    val description: String? = "",
    val yearOfRelease: String? = "",
    val likeNums: Int? = 0,
    val disLikeNums: Int?  = 0,
    val categories: ArrayList<String>? = arrayListOf()
) {
}