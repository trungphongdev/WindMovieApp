package com.example.windmoiveapp.ui.fragment

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.windmoiveapp.R
import com.example.windmoiveapp.adapter.NewAndHotAdapter
import com.example.windmoiveapp.databinding.FragmentNewAndHotMovieBinding
import com.example.windmoiveapp.extension.navigateWithAnim
import com.example.windmoiveapp.extension.showCustomToast
import com.example.windmoiveapp.model.MovieModel
import com.example.windmoiveapp.viewmodels.MovieViewModel


class NewAndHotFragment : BaseFragment<FragmentNewAndHotMovieBinding>() {
    private val adapter: NewAndHotAdapter by lazy { NewAndHotAdapter() }
    private val movieViewModels: MovieViewModel by lazy { MovieViewModel(activity?.application as Application) }
    private var movieModel: MovieModel? = null
    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentNewAndHotMovieBinding {
        return FragmentNewAndHotMovieBinding.inflate(inflater, container, false)
    }

    override fun onViewInitialized(
        view: View,
        savedInstanceState: Bundle?,
        isViewCreated: Boolean
    ) {
        initViews()
        initListener()
        initObserver()
    }

    private fun initListener() {
        adapter.onItemClickMovieItem = {
            findNavController().navigateWithAnim(
                R.id.movieDetailFragment, bundleOf(
                    MovieDetailFragment.BUNDLE_CONTENT_MOVIE to it
                ), R.id.movieTrailerFragment
            )
        }
        binding.headerBar.apply {
            setEventBackListener {
                moveToDashBoard()
            }
        }
        adapter.onItemClickFavouriteItem = { isLike, movie ->
            movieViewModels.invalidLikePost(isLike, movie)
            showProgress()
            movieModel = movie
        }
    }

    private fun initObserver() {
        movieViewModels.listMovie.observe(viewLifecycleOwner) {
            dismissProgress()
            adapter.setList(it)
        }
        movieViewModels.likePostLiveData.observe(viewLifecycleOwner) {
            dismissProgress()
            if (it) {
                movieViewModels.getMovieList()
                activity?.showCustomToast(getString(R.string.thankLabel))
            } else {
                activity?.showCustomToast(
                    getString(R.string.errorMessageLabel),
                    isShowToastError = true
                )
            }
        }
    }

    private fun initViews() {
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        binding.apply {
            rcvMovies.adapter = adapter
            rcvMovies.itemAnimator = DefaultItemAnimator()
            rcvMovies.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    override fun loadData() {
        super.loadData()
        showProgress()
        movieViewModels.getMovieList()
    }

}
