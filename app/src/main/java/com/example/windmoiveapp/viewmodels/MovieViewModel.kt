package com.example.windmoiveapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.windmoiveapp.constant.Categories
import com.example.windmoiveapp.database.AppDatabase
import com.example.windmoiveapp.database.BuildDaoDatabase
import com.example.windmoiveapp.firebase.FireBaseService
import com.example.windmoiveapp.model.MovieCategoryModel
import com.example.windmoiveapp.model.MovieModel
import com.example.windmoiveapp.model.UserModel
import com.example.windmoiveapp.util.AppApplication
import kotlinx.coroutines.launch

class MovieViewModel(application: Application) : AndroidViewModel(application) {
    val dao = AppDatabase.getDatabase(getApplication<Application>().applicationContext)
    var listMovie: MutableLiveData<List<MovieModel>> = MutableLiveData()
    var listMovieByName: MutableLiveData<List<MovieModel>> = MutableLiveData()
    var listMovieByCategories: MutableLiveData<List<MovieCategoryModel>> = MutableLiveData()
    var listMovieRoom: MutableLiveData<List<MovieModel>> = MutableLiveData()
    var movieRoomLiveData: MutableLiveData<MovieModel?> = MutableLiveData()
    var likePostLiveData: MutableLiveData<Boolean> = MutableLiveData()
    var listAllUser: MutableLiveData<List<UserModel>> = MutableLiveData()

    fun convertToListMovieByCategory(listMovie: List<MovieModel>) {
        viewModelScope.launch {
            val listMovieCate = arrayListOf<MovieCategoryModel>()
            Categories.values().forEach { category ->
                if (listMovie.any { it.categories.any { it == category.name } }) {
                    listMovieCate.add(
                        MovieCategoryModel(
                            category = category.type,
                            movies = listMovie.filter { it.categories.any { it == category.name } }
                                .shuffled()
                        )
                    )
                }
            }
            listMovieByCategories.postValue(listMovieCate)
        }
    }

    fun getListMovieByCategory(category: Categories) {
        viewModelScope.launch {
            val list = ArrayList<MovieCategoryModel>()
            val listMovieModel = ArrayList<MovieModel>()
            if (listMovie.value.isNullOrEmpty().not()) {
                for (movie in listMovie.value!!) {
                    if (movie.categories.any { it == category.name }) {
                        listMovieModel.add(movie)
                    }
                }
                list.add(MovieCategoryModel(category = category.type, listMovieModel))
                listMovieByCategories.postValue(list.filter { it.movies.isNullOrEmpty().not() })
            }
        }
    }


    fun getMovieList() {
        viewModelScope.launch {
            FireBaseService.getMovieList {
                listMovie.postValue(it)
            }
        }
    }

    fun getMovieListByCategory(categories: Categories) {
        viewModelScope.launch {
            FireBaseService.getMovieByCategory(categories) {
                listMovie.postValue(it)
            }
        }
    }

    fun getMovieListByName(name: String) {
        viewModelScope.launch {
            if (listMovie.value.isNullOrEmpty().not()) {
                listMovieByName.postValue(
                    listMovie.value?.filter { it.name?.contains(name) ?: false }
                )
            }
            /*        FireBaseService.getMovieByName(name) {
                        listMovieByName.postValue(it)
                    }*/
        }
    }

    fun getListMovieRoom() {
        viewModelScope.launch {
            val listMovie = BuildDaoDatabase.getMovieDao(AppApplication()).getAllMovie()
            listMovieRoom.postValue(listMovie)
        }
    }

    fun addMovieToRoom(movieModel: MovieModel) {
        viewModelScope.launch {
            dao.getMovieDao().insertColumnMovie(movieModel)
        }
    }

    fun getMovieById(movieModel: MovieModel?) {
        viewModelScope.launch {
            val movie = dao.getMovieDao().getMovieById(movieModel?.id ?: return@launch)
            movieRoomLiveData.postValue(movie)
        }
    }

    fun removeMovieById(movieModel: MovieModel?) {
        viewModelScope.launch {
            val movie = dao.getMovieDao().deleteMovie(movieModel?.id ?: return@launch)
        }
    }

    fun invalidLikePost(isLike: Boolean = false, movieModel: MovieModel) {
        viewModelScope.launch {
            FireBaseService.likePostMovie(isLike, movieModel) {
                likePostLiveData.postValue(it)
            }
        }
    }

    fun getAllUser() {
        FireBaseService.getInfoAllUser {
            listAllUser.postValue(it)
        }
    }
}