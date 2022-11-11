package com.example.windmoiveapp.database

import com.example.windmoiveapp.util.AppApplication

object BuildDaoDatabase {
//    fun getUserDao(application: AppApplication) =
//        application.appDatabase.get

    fun getMovieDao(application: AppApplication) =
        application.appDatabase.getMovieDao()

    fun getNotificationDao(application: AppApplication) =
        application.appDatabase.getNotificationDao()

}