package com.example.windmoiveapp.model

import android.graphics.Color
import com.example.windmoiveapp.constant.StatusLovingMovie
import com.example.windmoiveapp.model.charts.MovieChartModel
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import java.util.*
import kotlin.collections.HashMap

data class LovingMovieModel(
    var id: String = UUID.randomUUID().toString(),
    var idMovie: String = "",
    var idUser: String = "",
    var like: Int = StatusLovingMovie.NOTHING.status
)
fun getNumberLoveMovies(lovings: List<LovingMovieModel>, movies: List<MovieModel>, type: StatusLovingMovie): List<MovieChartModel> {
    val moviesChart: ArrayList<MovieChartModel> = arrayListOf()
    when (type.status) {
        StatusLovingMovie.LIKE.status -> {
            movies.forEach { movie ->
                val number = lovings.filter { it.idMovie == movie.id && it.like == StatusLovingMovie.LIKE.status }.size
                if (number > 0) {
                    moviesChart.add(MovieChartModel(movie, number))
                }
            }
        }
        StatusLovingMovie.DISLIKE.status -> {
            movies.forEach { movie ->
                val number = lovings.filter { it.idMovie == movie.id && it.like == StatusLovingMovie.DISLIKE.status }.size
                if (number > 0) {
                    moviesChart.add(MovieChartModel(movie, number))
                }
            }
        }
        else -> {}
    }
    return moviesChart.sortedBy { it.number }
}

fun dataLovingsPieEntry(lovings: List<LovingMovieModel>): PieData {
    val pieEntries = arrayListOf<PieEntry>()
    val data = HashMap<String, Int>()
    val numberLike = lovings.filter { loving -> loving.like == StatusLovingMovie.LIKE.status }.size
    val numberDislike = lovings.filter { loving -> loving.like == StatusLovingMovie.DISLIKE.status }.size
    data["Like"] = numberLike
    data["Dislike"] = numberDislike
    for (key in data.keys) {
        pieEntries.add(PieEntry(data[key]?.toFloat() ?: 0f, key))
    }
    val pieDataSet = PieDataSet(pieEntries, "Number Like On Movie").apply {
        setColors(Color.parseColor("#B11313"),Color.parseColor("#A0A3AA"))
        valueTextColor = Color.parseColor("#FFFFFF")
        valueTextSize = 8f
    }
    return  PieData(pieDataSet).apply {
        setDrawValues(true)
    }

}

fun List<LovingMovieModel>?.getNumberLovingByMovie(): Pair<Int, Int> {
    if (this != null && this.isNotEmpty()) {
        val like = this.filter { it.like == StatusLovingMovie.LIKE.status }.size
        val disLike = this.filter { it.like == StatusLovingMovie.DISLIKE.status }.size
        return like to disLike
    }
    return 0 to 0
}

fun List<LovingMovieModel>?.getItemLovingExist(lovingMovieModel: LovingMovieModel): LovingMovieModel? {
    return this?.firstOrNull { it.idMovie == lovingMovieModel.idMovie && it.idUser == lovingMovieModel.idUser }
}

fun List<LovingMovieModel>?.getStatusLovingByUser(userModel: UserModel): Int {
    return this?.firstOrNull { it.idUser == userModel.uid}?.like ?: StatusLovingMovie.NOTHING.status
}