package com.example.windmoiveapp.ui.fragment

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.windmoiveapp.R
import com.example.windmoiveapp.databinding.FragmentMovieDetailBottomSheetBinding
import com.example.windmoiveapp.extension.loadImage
import com.example.windmoiveapp.extension.navigateWithAnim
import com.example.windmoiveapp.model.MovieModel
import com.example.windmoiveapp.viewmodels.MovieViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class MovieDetailBottomSheet : BottomSheetDialogFragment() {
    private var binding: FragmentMovieDetailBottomSheetBinding? = null
    private val movieViewModels: MovieViewModel by lazy { MovieViewModel(activity?.application as Application) }
    private var isAdd: Boolean = false

    private var movieModel: MovieModel? = null
    private var isAddInList: Boolean = false

    companion object {
        private const val BUNDLE_CONTENT_MOVIE = "BUNDLE_CONTENT_MOVIE"
        const val TAG = "MovieDetailBottomSheet"
        fun newInstance(movieModel: MovieModel): MovieDetailBottomSheet {
            return MovieDetailBottomSheet().apply {
                this.arguments = bundleOf(BUNDLE_CONTENT_MOVIE to movieModel)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMovieDetailBottomSheetBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movieViewModels.getListMovieRoom()
        getDataFromBundle()
        initViews()
        initListener()
        initObserver()
    }

    private fun initObserver() {
        movieViewModels.movieRoomLiveData.observe(viewLifecycleOwner) {
            if (it == null) {
                isAdd = false
                binding?.imgAdd?.setImageResource(R.drawable.ic_add)
            } else {
                isAdd = true
                binding?.imgAdd?.setImageResource(R.drawable.ic_baseline_check_circle_24)
            }
        }
    }

    private fun getDataFromBundle() {
        movieModel = this.arguments?.getParcelable(BUNDLE_CONTENT_MOVIE)
    }

    private fun initViews() {
        binding?.apply {
            if (movieModel != null) {
                imgMovie.loadImage(movieModel?.image ?: "")
                tvNameMovie.text = movieModel?.name
                tvYearRelease.text = movieModel?.yearOfRelease
                tvTimeTotal.text = movieModel?.duration
                tvAllowAge.text = "7+"
                tvDescription.text = movieModel?.description
            }
        }
        movieViewModels.getMovieById(movieModel)
    }

    private fun initListener() {
        binding?.apply {
            imgPlay.setOnClickListener {
                navigateToMovieDetailScreen()
                this@MovieDetailBottomSheet.dismiss()
            }
            llInfoMovie.setOnClickListener {
                navigateToMovieDetailScreen()
                this@MovieDetailBottomSheet.dismiss()
            }
            imgClose.setOnClickListener {
                this@MovieDetailBottomSheet.dismiss()
            }
            imgAdd.setOnClickListener {
                isAdd = !isAdd
                if (isAdd) {
                    binding?.imgAdd?.setImageResource(R.drawable.ic_add)
                    movieViewModels.removeMovieByIdRoomDB(movieModel ?: return@setOnClickListener)
                } else {
                    binding?.imgAdd?.setImageResource(R.drawable.ic_baseline_check_circle_24)
                    movieViewModels.addMovieToRoom(movieModel ?: return@setOnClickListener)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding == null
    }

    private fun navigateToMovieDetailScreen() {
        findNavController().navigateWithAnim(
            R.id.movieDetailFragment,
            bundleOf(MovieDetailFragment.BUNDLE_CONTENT_MOVIE to movieModel)
        )
    }

}