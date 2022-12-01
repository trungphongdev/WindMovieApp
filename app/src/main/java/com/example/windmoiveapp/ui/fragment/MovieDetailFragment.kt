package com.example.windmoiveapp.ui.fragment

import android.app.Application
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.windmoiveapp.R
import com.example.windmoiveapp.adapter.RatingMovieAdapter
import com.example.windmoiveapp.adapter.ViewPagerAdapter
import com.example.windmoiveapp.constant.Categories
import com.example.windmoiveapp.constant.StatusLovingMovie
import com.example.windmoiveapp.databinding.FragmentMovieDetailBinding
import com.example.windmoiveapp.extension.*
import com.example.windmoiveapp.model.*
import com.example.windmoiveapp.util.PrefUtil
import com.example.windmoiveapp.viewmodels.AuthViewModel
import com.example.windmoiveapp.viewmodels.MovieViewModel
import com.google.android.material.tabs.TabLayoutMediator
import timber.log.Timber
import java.util.*

class MovieDetailFragment : BaseFragment<FragmentMovieDetailBinding>() {
    private val movieViewModels: MovieViewModel by lazy { MovieViewModel(activity?.application as Application) }
    private val authenViewModel: AuthViewModel by activityViewModels()
    private val adapterRating by lazy { RatingMovieAdapter() }
    private var movieModel: MovieModel? = null
    private var isAdd: Boolean = false
    private var lovingMovie: LovingMovieModel = LovingMovieModel()

    companion object {
        const val BUNDLE_CONTENT_MOVIE = "BUNDLE_CONTENT_MOVIE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movieModel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            this.arguments?.getParcelable(BUNDLE_CONTENT_MOVIE, MovieModel::class.java)
        } else {
            this.arguments?.getParcelable(BUNDLE_CONTENT_MOVIE)
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
        initViews()
        initListeners()
        initObserver()
    }

    private fun initObserver() {
        movieViewModels.movieRoomLiveData.observe(viewLifecycleOwner) {
            isAdd = it != null
            binding.imvMyList.isSelected = isAdd
        }

        movieViewModels.listRatingLiveData.observe(viewLifecycleOwner) {
            dismissProgress()
            movieViewModels.getRatingsUser()
            binding.llComment.tvNumberComment.text = getString(R.string.numberCommentLabel, it.size)
        }

        movieViewModels.listRatingUserLiveData.observe(viewLifecycleOwner) {
            dismissProgress()
            adapterRating.setList(it)
        }

        movieViewModels.postCommentSuccessLiveData.observe(viewLifecycleOwner) {
            if (it) {
                dismissProgress()
                movieViewModels.getRatingsById(movieModel?.id.ifBlankOrNull())
            } else {
                context?.showAlertDialog(getString(R.string.commentFailLabel))
            }
        }
        movieViewModels.lovingsByIdMovieLiveData.observe(viewLifecycleOwner) {
            dismissProgress()
            bindDataLovingMovie(it)
        }

        movieViewModels.isLoveMovie.observe(viewLifecycleOwner) {
            dismissProgress()
            movieViewModels.getLovingsByIdMovie(movieModel?.id ?: return@observe)
            Timber.tag("IsSendLove").d(it.toString())
        }

    }

    private fun bindDataLovingMovie(lovings: List<LovingMovieModel>?) {
        val pair = lovings.getNumberLovingByMovie()
        binding.tvLikeNumber.text = pair.first.toString()
        binding.tvDisLikeNumber.text = pair.second.toString()
        val isLike = lovings.getStatusLovingByUser(authenViewModel.userModelLiveData.value ?: return)
        when (isLike) {
            StatusLovingMovie.LIKE.status -> {
                binding.imvLike.isSelected = true
                binding.imvDislike.isSelected = false
            }
            StatusLovingMovie.DISLIKE.status -> {
                binding.imvDislike.isSelected = true
                binding.imvLike.isSelected = false
            }
            else -> {
                binding.imvLike.isSelected = false
                binding.imvDislike.isSelected = false
            }
        }
        if (lovings != null) {
            lovingMovie.id = lovings.firstOrNull { it.idMovie == movieModel?.id && it.idUser == authenViewModel.userModelLiveData.value?.uid }?.id.ifBlankOrNull { UUID.randomUUID().toString() }
        }
    }

    private fun initViews() {
        binding.apply {
            tvDescription.text = movieModel?.description
            tvName.text = movieModel?.name
            tvYearRelease.text = movieModel?.yearOfRelease
            tvDuration.text = movieModel?.duration
            tvCategory.text = Categories.getCategoryByName(movieModel?.categories ?: emptyList())
        }
        lovingMovie.idMovie = movieModel?.id ?: ""
        lovingMovie.idUser = authenViewModel.userModelLiveData.value?.uid.ifBlankOrNull { "" }
        setUpRecyclerViewComment()
        setUpVideoView()
        setUpViewPager()
    }

    private fun setUpRecyclerViewComment() {
        binding.llComment.rcvComment.apply {
            adapter = adapterRating
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
            itemAnimator = DefaultItemAnimator()
        }
    }

    private fun setUpVideoView() {
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
            llPlayMovie.click {
                setUpVideoView()
            }
            imvLike.click {
                lovingMovie.like = StatusLovingMovie.LIKE.status
                invalidLikeMovie()
            }
            imvDislike.click {
                lovingMovie.like = StatusLovingMovie.DISLIKE.status
                invalidLikeMovie()
            }
            imvComment.click {
                llComment.root.isVisible = true
            }
            imvMyList.click {
                saveFavouriteMovie()
            }
            headerBar.apply {
                setEventBackListener {
                    onBackFragment()
                }
                setEventSearchListener {
                    findNavController().navigateWithAnim(R.id.searchFragment)
                }
            }
            llComment.btnPostCmt.click {
                postComment()
            }

        }
    }

    private fun invalidLikeMovie() {
      //  showProgress()
        movieViewModels.lovingMovie(lovingMovie)
    }

    private fun saveFavouriteMovie() {
        isAdd = !isAdd
        binding.imvMyList.isSelected = isAdd
        movieModel?.let {
            if (isAdd) {
                movieViewModels.removeMovieByIdRoomDB(it)
            } else {
                movieViewModels.addMovieToRoom(it)
            }
        }
    }

    private fun postComment() {
        showProgress()
        if (binding.llComment.edtComment.text.isNullOrBlank().not()) {
            val ratingModel = RatingModel(
                id = UUID.randomUUID().toString(),
                comment = binding.llComment.edtComment.text.toString(),
                time = Date().time,
                userId = authenViewModel.userModelLiveData.value?.uid,
                movieId = movieModel?.id
            )
            binding.llComment.edtComment.text?.clear()
            movieViewModels.postRating(postRating(ratingModel))
        } else {
            context?.showAlertDialog(getString(R.string.alertCmtEmptyLabel))
        }
    }

    private fun setUpViewPager() {
        val listFragment = arrayListOf<Fragment>(
            TrailerMovieFragment(),
            MoreAndLikeThisFragment()
        )
        val listTitle = arrayListOf(
            getString(R.string.trailerAndMoreLabel),
            getString(R.string.moreLikeThisLabel)
        )
        binding.vpMovie.adapter = ViewPagerAdapter(this@MovieDetailFragment).apply {
            setListFragment(listFragment)
            binding.vpMovie.setFixedAdapter(this)
        }

        TabLayoutMediator(binding.tabLayoutOptions, binding.vpMovie) { tab, position ->
            tab.text = listTitle[position]
        }.attach()
    }

    override fun loadData() {
        super.loadData()
        showProgress()
        movieViewModels.getMovieById(movieModel)
        movieViewModels.getRatingsById(movieModel?.id ?: return)
        movieViewModels.getLovingsByIdMovie(movieModel?.id ?: return)
    }

}


