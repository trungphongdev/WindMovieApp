package com.example.windmoiveapp.model

import com.example.windmoiveapp.constant.Categories

data class MovieCategoryModel(
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

fun List<MovieCategoryModel>.setListMovieByType(): List<MovieCategoryModel> {
    this.forEachIndexed { index, movie ->
        when {
            (movie.category == Categories.ANIMALS.type) -> {
                this[index].viewType = MovieCategoryModel.TYPE_CIRCLE
            }
            (movie.category == Categories.FANTASY.type) -> {
                this[index].viewType = MovieCategoryModel.TYPE_HIGH
            }
            else -> {
                this[index].viewType = MovieCategoryModel.TYPE_NORMAL
            }
        }
    }
    return this
}

fun List<MovieModel>.getListByCategory(categories: Categories): List<MovieCategoryModel> {
    val list = ArrayList<MovieCategoryModel>()
    for (movie in this) {
        if (movie.categories.any { it == categories.name }) {
            list.add(
                MovieCategoryModel(
                    category = categories.type,
                    movies = this.filter { it.categories.any { it == categories.name } }.shuffled()
                )
            )
        }
    }
    return list
}