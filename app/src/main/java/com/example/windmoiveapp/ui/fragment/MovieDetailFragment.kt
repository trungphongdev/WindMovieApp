package com.example.windmoiveapp.ui.fragment

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import com.example.windmoiveapp.constant.Categories
import com.example.windmoiveapp.databinding.FragmentMovieDetailBinding
import com.example.windmoiveapp.model.MovieModel
import com.example.windmoiveapp.service.MovieService

class MovieDetailFragment : BaseFragment<FragmentMovieDetailBinding>() {
    private var mService: MovieService? = null
    private var mBound: Boolean = false
    private var movieModel: MovieModel? = null

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
            activity?.bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    companion object {
        const val BUNDLE_CONTENT_MOVIE = "BUNDLE_CONTENT_MOVIE"
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
        getDataFromBundle()
        initViews()
        initListeners()
    }

    private fun getDataFromBundle() {
        movieModel = this.arguments?.getParcelable(BUNDLE_CONTENT_MOVIE)
    }

    private fun initViews() {
        binding.apply {
            tvDescription.text = movieModel?.description
            tvName.text = movieModel?.name
            tvYearRelease.text = movieModel?.yearOfRelease
            tvDuration.text = movieModel?.duration
            tvCategory.text = Categories.getCategoryByName(movieModel?.categories ?: emptyList())
        }
        setUpVideoView()

        //mService?.onStartMediaPlayer(movieModel?.trailerUrl ?: return)
    }

    private fun setUpVideoView(url: String = movieModel?.trailerUrl ?: "") {
        binding.apply {
            val mediaController = MediaController(this.root.context)
            mediaController.setAnchorView(videoView)
            videoView.setMediaController(mediaController)
            videoView.setVideoURI(Uri.parse(movieModel?.trailerUrl))
            videoView.requestFocus()
            videoView.start()
        }
    }

    private fun initListeners() {
        binding.apply {
            headerBar.setEventBackListener {
                moveToDashBoard()
            }
            llPlayMovie.setOnClickListener {
                setUpVideoView(movieModel?.movieUrl ?: return@setOnClickListener)
            }


        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.unbindService(connection)
        mBound = false
    }
}


