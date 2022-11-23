package com.example.windmoiveapp.model

import com.example.windmoiveapp.constant.StatusLovingMovie
import java.util.*

data class LovingMovieModel(
    var id: String = UUID.randomUUID().toString(),
    var idMovie: String = "",
    var idUser: String = "",
    var like: Int = StatusLovingMovie.NOTHING.status
)

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