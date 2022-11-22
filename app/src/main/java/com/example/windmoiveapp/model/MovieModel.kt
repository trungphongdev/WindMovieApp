package com.example.windmoiveapp.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class MovieModel(
    @PrimaryKey
    val id: String = "",
    val name: String? = "",
    val trailerUrl: String? = "",
    val movieUrl: String? = "",
    val description: String? = "",
    val yearOfRelease: String? = "",
    val duration: String? = "",
    val image: String? = "",
    val categories: List<String> = listOf()
) : Parcelable {
}

class Converters {
    @TypeConverter
    fun listToJson(value: List<String>): String = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String): List<String> =
        Gson().fromJson(value, Array<String>::class.java).toList()
}