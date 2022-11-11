package com.example.windmoiveapp.viewmodels

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.example.windmoiveapp.constant.Categories
import com.example.windmoiveapp.firebase.FireBaseService
import com.example.windmoiveapp.model.MovieCategoryModel
import com.example.windmoiveapp.model.MovieModel
import com.example.windmoiveapp.util.AppApplication
import kotlinx.coroutines.launch

class MovieViewModel(application: AppApplication) : AndroidViewModel(application) {

    var listMovie: MutableLiveData<List<MovieModel>> = MutableLiveData()

    var listMovieCategory: LiveData<List<MovieCategoryModel>> = Transformations.map(listMovie) {
        val listMovieCate = arrayListOf<MovieCategoryModel>()
        Categories.values().drop(1).dropLast(1).forEach { category ->
            listMovieCate.add(
                MovieCategoryModel(
                    category.type, it.filter { it.categories?.any { category.name == it } ?: false }
                )
            )
        }
        listMovieCate
    }

    fun getMovieList() {
        viewModelScope.launch {
            listMovie.value = FireBaseService.getMovieList()
        }
    }



}