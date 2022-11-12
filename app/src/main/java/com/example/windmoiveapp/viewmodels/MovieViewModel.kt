package com.example.windmoiveapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.windmoiveapp.constant.Categories
import com.example.windmoiveapp.firebase.FireBaseService
import com.example.windmoiveapp.model.MovieCategoryModel
import com.example.windmoiveapp.model.MovieModel
import kotlinx.coroutines.launch

class MovieViewModel(application: Application) : AndroidViewModel(application) {

    var listMovie: MutableLiveData<List<MovieModel>> = MutableLiveData()
    var listMovieByCategories: MutableLiveData<List<MovieCategoryModel>> = MutableLiveData()


    fun convertToListMovieByCategory(listMovie: List<MovieModel>) {
        val listMovieCate = arrayListOf<MovieCategoryModel>()
        listMovie.forEachIndexed { index, movieModel ->
            Categories.values().forEach { category ->
                if (movieModel.categories.contains(category.name)) {
                    listMovieCate.add(
                        MovieCategoryModel(
                            category = category.type,
                            movies = listMovie.filter { it.categories.any { it == category.name } }
                        )
                    )
                }
            }
        }
        listMovieByCategories.postValue(listMovieCate)
    }

    fun getMovieList() {
        viewModelScope.launch {
            listMovie.postValue(FireBaseService.getMovieList())
        }
    }
}