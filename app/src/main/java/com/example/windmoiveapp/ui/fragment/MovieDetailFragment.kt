package com.example.windmoiveapp.ui.fragment

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.os.Binder
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.windmoiveapp.R
import com.example.windmoiveapp.databinding.FragmentMovieDetailBinding
import timber.log.Timber


class MovieDetailFragment : BaseFragment<FragmentMovieDetailBinding>() {

    private lateinit var mService: MovieService
    private var mBound: Boolean = false

    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            val binder = service as MovieService.MovieBinder
            mService = binder.getMovieService()
            mBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Intent(context, MovieService::class.java).also { intent ->
            mService.bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MovieDetailFragment().apply {
            }
    }

    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentMovieDetailBinding {
        return FragmentMovieDetailBinding.inflate(inflater)
    }

    override fun onViewInitialized(
        view: View,
        savedInstanceState: Bundle?,
        isViewCreated: Boolean
    ) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        mService.unbindService(connection)
        mBound = false
    }
}

private const val ACTION_PLAY: String = "com.example.action.PLAY"

class MovieService: Service() {
    private  val TAG = "MovieService"

    private var mMediaPlayer: MediaPlayer? = null

    private var binder: MovieBinder = MovieBinder()

    inner class MovieBinder() : Binder() {
        fun getMovieService(): MovieService {
            return this@MovieService
        }
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
        mMediaPlayer?.setDataSource(url)
        mMediaPlayer?.prepareAsync()
        mMediaPlayer?.start()
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

