package com.example.windmoiveapp.ui.fragment

import android.app.Application
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.windmoiveapp.R
import com.example.windmoiveapp.adapter.ViewPagerAdapter
import com.example.windmoiveapp.constant.Categories
import com.example.windmoiveapp.databinding.FragmentMovieDetailBinding
import com.example.windmoiveapp.extension.navigateWithAnim
import com.example.windmoiveapp.extension.setFixedAdapter
import com.example.windmoiveapp.model.MovieModel
import com.example.windmoiveapp.viewmodels.MovieViewModel
import com.google.android.material.tabs.TabLayoutMediator

class MovieDetailFragment : BaseFragment<FragmentMovieDetailBinding>() {
    private val movieViewModels: MovieViewModel by lazy { MovieViewModel(activity?.application as Application) }
    private var movieModel: MovieModel? = null
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
        setUpViewPager()
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
            imvMyRate.setOnClickListener {
                findNavController().navigateWithAnim(R.id.myListFragment)
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
            headerBar.setEventBackListener {
                moveToDashBoard()
            }
            headerBar.setEventSearchListener {
                findNavController().navigateWithAnim(R.id.searchFragment)
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
        movieViewModels.getMovieById(movieModel)
    }

}


