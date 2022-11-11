package com.example.windmoiveapp.model

class MovieCategoryModel(
    val category: String? = null,
    val movies: List<MovieModel>? = null,
    var viewType: Int = TYPE_NORMAL
) {
    companion object {
        const val TYPE_NORMAL = 1
        const val TYPE_CIRCLE = 2
        const val TYPE_HIGH = 3
    }
}