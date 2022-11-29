package com.example.windmoiveapp.model

import android.annotation.SuppressLint
import android.graphics.Color
import com.example.windmoiveapp.model.charts.MovieChartModel
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter

data class RatingModel(
    var id: String? = null,
    var comment: String? = null,
    var time: Long? = null,
    var userId: String? = null,
    var movieId: String? = null
) {
    var userModel: UserModel? = null
}
fun setBarDataNumberRatings(ratings: List<RatingModel>, movies: List<MovieModel>): BarData {
    val moviesChart: ArrayList<MovieChartModel> = arrayListOf()
       movies.forEach { movie ->
           val number = ratings.filter { it.movieId == movie.id }.size
           moviesChart.add(MovieChartModel(movie, number))
   }

    val barEntries: ArrayList<BarEntry> = arrayListOf()
    moviesChart.mapIndexed { index, movie ->
        barEntries.add(BarEntry((index + 1).toFloat(), movie.number.toFloat()))
    }
    val dataSet = BarDataSet(barEntries, "Number Rating On Movie").apply {
        color = Color.parseColor("#B11313")
        valueTextColor = Color.parseColor("#FFFFFFFF")
        valueTextSize = 8f
        valueFormatter = ValueFormatterBarDataSet()
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

class ValueFormatterBarDataSet : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return value.toInt().toString()
    }
}