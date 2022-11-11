package com.example.windmoiveapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.windmoiveapp.model.MovieModel
import com.example.windmoiveapp.model.NotificationModel

@Dao
interface NotificationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notify: NotificationModel)

    @Query("DELETE FROM NotificationModel WHERE id = :id")
    suspend fun deleteNotification(id: String)

    @Query("SELECT * FROM NotificationModel")
    suspend fun getAllNotification(): LiveData<List<NotificationModel>>
}