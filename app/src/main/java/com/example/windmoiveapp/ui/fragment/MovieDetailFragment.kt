package com.example.windmoiveapp.ui.fragment

import android.app.Application
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.windmoiveapp.R
import com.example.windmoiveapp.adapter.RatingMovieAdapter
import com.example.windmoiveapp.adapter.ViewPagerAdapter
import com.example.windmoiveapp.constant.Categories
import com.example.windmoiveapp.databinding.FragmentMovieDetailBinding
import com.example.windmoiveapp.extension.*
import com.example.windmoiveapp.model.MovieModel
import com.example.windmoiveapp.model.RatingModel
import com.example.windmoiveapp.model.UserModel
import com.example.windmoiveapp.model.postRating
import com.example.windmoiveapp.util.PrefUtil
import com.example.windmoiveapp.viewmodels.MovieViewModel
import com.google.android.material.tabs.TabLayoutMediator
import java.text.SimpleDateFormat
import java.util.*

class MovieDetailFragment : BaseFragment<FragmentMovieDetailBinding>() {
    private val movieViewModels: MovieViewModel by lazy { MovieViewModel(activity?.application as Application) }
    private val adapterRating by lazy { RatingMovieAdapter() }
    private var movieModel: MovieModel? = null
    private val pref by lazy { PrefUtil.getInstance(activity?.application as Application) }
    private var ratingModel: RatingModel = RatingModel()
    private var isAdd: Boolean = false

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
        initObserver()
    }

    private fun initObserver() {
        movieViewModels.movieRoomLiveData.observe(viewLifecycleOwner) {
            if (it == null) {
                isAdd = false
                binding.imvMyList.setImageResource(R.drawable.ic_add)
            } else {
                isAdd = true
                binding.imvMyList.setImageResource(R.drawable.ic_baseline_check_circle_24)
            }
        }

        movieViewModels.listRating.observe(viewLifecycleOwner) {
          movieViewModels.getRatingsUser()
        }

        movieViewModels.listRatingUser.observe(viewLifecycleOwner) {
            dismissProgress()
            adapterRating.setList(it)
        }

        movieViewModels.postCommentSuccessLiveData.observe(viewLifecycleOwner) {
            if (it) {
                showProgress()
                movieViewModels.getRatingsById(movieModel?.id ?: "")
            } else {
                context?.showAlertDialog(getString(R.string.commentFailLabel))
            }
        }

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
            llPlayMovie.setOnClickListener {
                setUpVideoView()
            }
            imvLike.setOnClickListener {
                findNavController().navigateWithAnim(R.id.myListFragment)
            }
            imvComment.setOnClickListener {
                llComment.root.isVisible = true
            }
            imvMyList.setOnClickListener {
                isAdd = !isAdd
                if (isAdd) {
                    binding.imvMyList.setImageResource(R.drawable.ic_add)
                    movieViewModels.removeMovieById(movieModel ?: return@setOnClickListener)
                } else {
                    binding.imvMyList.setImageResource(R.drawable.ic_baseline_check_circle_24)
                    movieViewModels.addMovieToRoom(movieModel ?: return@setOnClickListener)
                }
            }
            headerBar.apply {
                setEventBackListener {
                    moveToDashBoard()
                }
                setEventSearchListener {
                    findNavController().navigateWithAnim(R.id.searchFragment)
                }

                llComment.btnPostCmt.click {
                    val userJson = pref.getValue(UserModel.PREF_USER, "")
                    val user = GsonExt.convertGsonToObjet(userJson, UserModel::class.java)
                    if (llComment.edtComment.text.isNullOrBlank().not()) {
                        val ratingModel = RatingModel(
                            id = UUID.randomUUID().toString(),
                            comment = llComment.edtComment.text.toString(),
                            time = Date().time,
                            isLike = false,
                            userId = user.uid,
                            movieId = movieModel?.id
                        )
                        llComment.edtComment.text?.clear()
                        showProgress()
                        movieViewModels.postRating(postRating(ratingModel))
                    } else {
                        context?.showAlertDialog(getString(R.string.alertCmtEmptyLabel))
                    }
                }
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
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
    }

}


