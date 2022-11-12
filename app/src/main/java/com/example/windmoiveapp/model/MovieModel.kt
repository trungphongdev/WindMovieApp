package com.example.windmoiveapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson

@Entity
data class MovieModel(
    @PrimaryKey
    val id: String = "",
    val name: String? = "",
    val trailerUrl: String? = "",
    val movieUrl: String? = "",
    val description: String? = "",
    val yearOfRelease: String? = "",
    val likeNum: Int? = 0,
    val dislikeNum: Int? = 0,
    val categories: List<String> = listOf()
)

class Converters {
    @TypeConverter
    fun listToJson(value: List<String>): String = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String): List<String> =
        Gson().fromJson(value, Array<String>::class.java).toList()
}