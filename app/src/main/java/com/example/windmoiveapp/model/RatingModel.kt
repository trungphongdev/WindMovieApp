package com.example.windmoiveapp.model

data class RatingModel(
    var id: String? = null,
    var comment: String? = null,
    var time: Long? = null,
    var isLike: Boolean? = null,
    var userId: String? = null,
    var movieId: String? = null
) {
    var userModel: UserModel? = null
    companion object {
        const val DISLIKE = false
        const val LIKE = true
    }
}
fun postRating(ratingModel: RatingModel): HashMap<String, Any?> {
    return hashMapOf(
        ratingModel::id.name to ratingModel.id,
        ratingModel::comment.name to ratingModel.comment,
        ratingModel::time.name to ratingModel.time,
        ratingModel::isLike.name to ratingModel.isLike,
        ratingModel::userId.name to ratingModel.userId,
        ratingModel::movieId.name to ratingModel.movieId,
    )
}