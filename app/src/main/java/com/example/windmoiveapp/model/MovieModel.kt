package com.example.windmoiveapp.model

import android.content.Context
import android.os.Parcelable
import androidx.core.content.ContextCompat
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.windmoiveapp.R
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class MovieModel(
    @PrimaryKey
    var id: String = "",
    var name: String? = "",
    var trailerUrl: String? = "",
    var movieUrl: String? = "",
    var description: String? = "",
    val yearOfRelease: String? = "",
    val duration: String? = "",
    var image: String? = "",
    var categories: List<String> = listOf()
) : Parcelable {
}
fun List<MovieModel>.isMovieExist(id: String): Boolean {
    val movie = this.find { it.id == id }
    return movie != null
}

class Converters {
    @TypeConverter
    fun listToJson(value: List<String>): String = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String): List<String> =
        Gson().fromJson(value, Array<String>::class.java).toList()
}