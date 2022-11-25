package com.example.windmoiveapp.ui.fragment

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.windmoiveapp.R
import com.example.windmoiveapp.adapter.ManagerMoviesAdapter
import com.example.windmoiveapp.databinding.FragmentManagerMovieBinding
import com.example.windmoiveapp.extension.click
import com.example.windmoiveapp.extension.showAlertDialog
import com.example.windmoiveapp.extension.showCustomToast
import com.example.windmoiveapp.util.ADD_MOVIE
import com.example.windmoiveapp.util.REMOVE_MOVIE
import com.example.windmoiveapp.viewmodels.MovieViewModel


class MovieManagerFragment : BaseFragment<FragmentManagerMovieBinding>() {
    private val adapter: ManagerMoviesAdapter by lazy { ManagerMoviesAdapter() }
    private val movieViewModels: MovieViewModel by lazy { MovieViewModel(activity?.application as Application) }

    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentManagerMovieBinding {
        return FragmentManagerMovieBinding.inflate(inflater, container, false)
    }

    override fun onViewInitialized(
        view: View,
        savedInstanceState: Bundle?,
        isViewCreated: Boolean
    ) {
        initViews()
        initObserver()
        initListener()
    }

    private fun initViews() {
        binding.apply {
            rcvMovies.adapter = adapter
            rcvMovies.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            rcvMovies.itemAnimator = DefaultItemAnimator()
        }
    }


    private fun initListener() {
        binding.headerBar.apply {
            setEventBackListener {
                super.onBackFragment()
            }
        }
        adapter.onItemClick = { movieModel, type ->
            if (type == REMOVE_MOVIE) {
                activity?.showAlertDialog(
                    getString(
                        R.string.confirmRemoveMovie,
                        movieModel.name,
                        movieModel.id
                    ), isShowNegativeBtn = true
                ) {
                    showProgress()
                    movieViewModels.removeMovieByIdOnServer(movieModel)
                }
            } else {
                val bundle = bundleOf(
                    UpdateMovieFragment.BUNDLE_TYPE_MANAGEMENT to type,
                    UpdateMovieFragment.BUNDLE_CONTENT_MOVIE to movieModel
                )
                navigateToDestination(R.id.updateMovieFragment, bundle)
            }
        }
        binding.fabAddMovie.click {
            navigateToDestination(R.id.updateMovieFragment, bundleOf(UpdateMovieFragment.BUNDLE_TYPE_MANAGEMENT to ADD_MOVIE))
        }
    }

    private fun initObserver() {
        movieViewModels.listMovieLiveData.observe(viewLifecycleOwner) {
            dismissProgress()
            binding.llEmptyData.root.isGone = it.isNotEmpty()
            adapter.setList(it)
        }

        movieViewModels.isRemoveMovieLiveData.observe(viewLifecycleOwner) {
            if (it) {
                showProgress()
                movieViewModels.getMovieList()
            } else {
                dismissProgress()
                activity.showCustomToast(getString(R.string.removeMovieFail), true)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        showProgress()
        movieViewModels.getMovieList()
    }

}
