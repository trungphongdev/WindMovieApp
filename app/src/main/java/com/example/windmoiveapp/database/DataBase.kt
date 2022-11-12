package com.example.windmoiveapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.windmoiveapp.model.Converters
import com.example.windmoiveapp.model.MovieModel
import com.example.windmoiveapp.model.NotificationModel

const val VERSION = 10
const val EXPORT_SCHEMA = false
const val WINDMOVIE_DB = "WINDMOVIE_DB"

@Database(
    entities =
    [NotificationModel::class, MovieModel::class],
    version = VERSION,
    exportSchema = EXPORT_SCHEMA
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getMovieDao(): MovieDao
    abstract fun getNotificationDao(): NotificationDao
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    WINDMOVIE_DB
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
