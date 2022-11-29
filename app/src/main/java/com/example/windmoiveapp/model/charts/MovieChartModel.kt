package com.example.windmoiveapp.model.charts

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.windmoiveapp.R
import com.example.windmoiveapp.model.MovieModel
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter

data class MovieChartModel(
    val movieModel: MovieModel,
    val number: Int
)

/*
fun barDataChart(movies: List<MovieChartModel>, context: Context): BarData {
    val listBarEntry = movies.convertToListBarEntry()
    val dataSet = BarDataSet(listBarEntry, "")
    dataSet.apply {
        color = ContextCompat.getColor(context, R.color.redB11313)
        valueTextColor = ContextCompat.getColor(context, R.color.white)
        valueTextSize = 8f
    }
    return BarData(dataSet)
}

fun List<MovieChartModel>.convertToListBarEntry(): List<BarEntry> {
    return this.sortedBy { it.number }.mapIndexed { index, movieChartModel ->
        BarEntry(index.toFloat(), movieChartModel.number.toFloat())
    }
}

class ValueFormatterBarDataSet : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return value.toInt().toString()
    }
}*/
