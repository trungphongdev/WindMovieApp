package com.example.windmoiveapp.model

data class RatingModel(
    var id: String? = null,
    var comment: String? = null,
    var time: String? = null,
    var isLike: Boolean? = null,
    var userId: Int? = null,
    var movieId: String? = null
) {
    companion object {
        const val DISLIKE = false
        const val LIKE = true
    }
}