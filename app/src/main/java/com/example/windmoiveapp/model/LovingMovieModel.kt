package com.example.windmoiveapp.model

data class LovingMovieModel(
    val id: String,
    val idMovie: String,
    val idUser: String,
    var isLike: Boolean? = null
)