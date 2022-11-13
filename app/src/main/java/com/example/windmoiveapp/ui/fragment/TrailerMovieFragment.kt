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
import com.example.windmoiveapp.adapter.MovieTrailerAdapter
import com.example.windmoiveapp.databinding.FragmentTrailerMovieBinding
import com.example.windmoiveapp.extension.navigateWithAnim
import com.example.windmoiveapp.viewmodels.MovieViewModel


class TrailerMovieFragment : BaseFragment<FragmentTrailerMovieBinding>() {
    private val adapter: MovieTrailerAdapter by lazy { MovieTrailerAdapter() }
    private val movieViewModels: MovieViewModel by lazy { MovieViewModel(activity?.application as Application) }

    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentTrailerMovieBinding {
        return FragmentTrailerMovieBinding.inflate(inflater, container, false)
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
    }

    private fun initObserver() {
        movieViewModels.listMovie.observe(viewLifecycleOwner) {
            dismissProgress()
            adapter.setList(it.shuffled())
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
