package com.example.windmoiveapp.ui.fragment

import android.app.Service
import android.content.Intent
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


class MovieDetailFragment : BaseFragment<FragmentMovieDetailBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_detail, container, false)
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
}

private const val ACTION_PLAY: String = "com.example.action.PLAY"

class MovieService: Service(), MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {
    private  val TAG = "MovieService"

    private var mMediaPlayer: MediaPlayer? = null

    private var binder: MyBinder = MyBinder()

    class MyBinder() : Binder() {
        fun getMovieBoundService(): MovieService {
            return MovieService()
        }
    }

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
    }



    override fun onBind(intent: Intent?): IBinder? {
        Log.e(TAG, "onBind: ", )
        return binder
    }

    override fun onPrepared(mediaPlayer: MediaPlayer) {
        mediaPlayer.start()
    }

    fun initMediaPlayer() {
        // ...initialize the MediaPlayer here...
        mMediaPlayer?.setOnErrorListener(this)
    }

    override fun onError(mp: MediaPlayer, what: Int, extra: Int): Boolean {
        // ... react appropriately ...
        // The MediaPlayer has moved to the Error state, must be reset!
        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG, "onDestroy: ", )
        mMediaPlayer?.release()
    }
}

