package com.example.windmoiveapp.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import timber.log.Timber


class MovieService : Service() {
    private val TAG = "MovieService"

    private var mMediaPlayer: MediaPlayer? = null

    private var binder: MovieBinder = MovieBinder()

    inner class MovieBinder() : Binder() {
        fun getMovieService(): MovieService {
            return this@MovieService
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }

/*
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        when(intent.action) {
            ACTION_PLAY -> {
                mMediaPlayer = MediaPlayer().apply {
                    setDataSource("")
                }
                mMediaPlayer?.apply {
                    setOnPreparedListener(this@MovieService)
                    prepareAsync() // prepare async to not block main thread
                }

            }
        }
        return START_STICKY
    }*/

    override fun onCreate() {
        super.onCreate()
        mMediaPlayer = MediaPlayer()
    }

    fun onStartMediaPlayer(url: String) {
        mMediaPlayer?.apply {
            setDataSource(url)
            prepareAsync()
            start()
        }
    }

    fun onCompleteListener() {
        mMediaPlayer?.setOnCompletionListener {
            mMediaPlayer?.seekTo(1)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        Timber.tag(TAG).e("onBind: ")
        return binder
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.tag(TAG).e("onDestroy: ")
        mMediaPlayer?.release()
    }
}