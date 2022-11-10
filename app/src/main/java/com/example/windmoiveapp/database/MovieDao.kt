package com.example.windmoiveapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.windmoiveapp.model.MovieModel

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertColumnMovie(movie: MovieModel)

    @Query("DELETE FROM MovieModel WHERE id = :id")
    suspend fun deleteMovie(id: String)

    @Query("SELECT * FROM MovieModel")
    suspend fun getAllMovie(): LiveData<List<MovieModel>>
}