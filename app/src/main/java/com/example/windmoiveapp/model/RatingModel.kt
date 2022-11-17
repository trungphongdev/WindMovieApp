package com.example.windmoiveapp.model

data class RatingModel(
    val id: Int? = 0,
    val comment: String? ="",
    val time: String? = "",
    var isLike: Boolean? = false
) {
}