package com.example.windmoiveapp.ui.fragment

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.example.windmoiveapp.R
import com.example.windmoiveapp.adapter.MovieItemMoreLikeThisAdapter
import com.example.windmoiveapp.databinding.FragmentMoreLikeThisMovieBinding
import com.example.windmoiveapp.extension.navigateWithAnim
import com.example.windmoiveapp.viewmodels.MovieViewModel


class MoreAndLikeThisFragment : BaseFragment<FragmentMoreLikeThisMovieBinding>() {
    private val adapter: MovieItemMoreLikeThisAdapter by lazy { MovieItemMoreLikeThisAdapter() }
    private val movieViewModels: MovieViewModel by lazy { MovieViewModel(activity?.application as Application) }

    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentMoreLikeThisMovieBinding {
        return FragmentMoreLikeThisMovieBinding.inflate(inflater, container, false)
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
                ), R.id.moreAndLikeThisFragment
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
            rcvMovies.layoutManager = GridLayoutManager(context, 3)
        }
    }

    override fun loadData() {
        super.loadData()
        showProgress()
        movieViewModels.getMovieList()
    }

}
