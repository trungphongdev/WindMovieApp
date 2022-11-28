package com.example.windmoiveapp.model

import com.example.windmoiveapp.constant.StatusLovingMovie
import com.example.windmoiveapp.model.charts.MovieChartModel
import com.github.mikephil.charting.data.PieEntry
import java.util.*
import kotlin.collections.HashMap

data class LovingMovieModel(
    var id: String = UUID.randomUUID().toString(),
    var idMovie: String = "",
    var idUser: String = "",
    var like: Int = StatusLovingMovie.NOTHING.status
)

fun dataLovingsPieEntry(lovings: List<LovingMovieModel>): List<PieEntry> {
    val pieEntries = arrayListOf<PieEntry>()
    val data = HashMap<String, Int>()
    val numberLike = lovings.filter { loving -> loving.like == StatusLovingMovie.LIKE.status }.size
    val numberDislike = lovings.filter { loving -> loving.like == StatusLovingMovie.DISLIKE.status }.size
    data["Number Movies Like"] = numberLike
    data["Number Movies Dislike"] = numberDislike
    for (key in data.keys) {
        pieEntries.add(PieEntry(data[key]?.toFloat() ?: 0f, key))
    }
    return pieEntries
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