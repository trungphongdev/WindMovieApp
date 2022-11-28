package com.example.windmoiveapp.model

import android.graphics.Color
import com.example.windmoiveapp.model.charts.MovieChartModel
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

data class RatingModel(
    var id: String? = null,
    var comment: String? = null,
    var time: Long? = null,
    var userId: String? = null,
    var movieId: String? = null
) {
    var userModel: UserModel? = null
}
fun setBarDataNumberRatings(ratings: List<RatingModel>,movies: List<MovieModel>): BarData {
    val barEntries = ArrayList<BarEntry>()
    val list = ArrayList<MovieChartModel>()
       movies.forEach { movie ->
           val number = ratings.filter { it.movieId == movie.id }.size
           list.add(MovieChartModel(movie, number))
   }
    list.forEachIndexed { index, movie ->
        barEntries.add(BarEntry(index.toFloat(), movie.number.toFloat()))
    }
    val dataSet = BarDataSet(barEntries, "Number Rating On Movie").apply {
        color = Color.parseColor("#B11313")
        valueTextColor = Color.parseColor("#FFFFFF")
        valueTextSize = 8f
    }
    return BarData(dataSet)
}

fun getLabelChart(movies: List<MovieModel>): List<String> {
    return movies.map { it.name ?: "" }
}
fun postRating(ratingModel: RatingModel): HashMap<String, Any?> {
    return hashMapOf(
        ratingModel::id.name to ratingModel.id,
        ratingModel::comment.name to ratingModel.comment,
        ratingModel::time.name to ratingModel.time,
        ratingModel::userId.name to ratingModel.userId,
        ratingModel::movieId.name to ratingModel.movieId,
    )
}