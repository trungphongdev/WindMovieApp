package com.example.windmoiveapp.ui.fragment

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.windmoiveapp.R
import com.example.windmoiveapp.adapter.ManagerMoviesAdapter
import com.example.windmoiveapp.adapter.MyListMovieAdapter
import com.example.windmoiveapp.databinding.FragmentManagerMovieBinding
import com.example.windmoiveapp.databinding.FragmentMyListMovieBinding
import com.example.windmoiveapp.extension.navigateWithAnim
import com.example.windmoiveapp.extension.showAlertDialog
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
    }
    private fun initViews() {
        binding.apply {
            rcvMovies.adapter = adapter
            rcvMovies.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            rcvMovies.itemAnimator = DefaultItemAnimator()
        }
    }

    private fun initObserver() {
        movieViewModels.listMovie.observe(viewLifecycleOwner) {
            dismissProgress()
            binding.llEmptyData.root.isGone = it.isNotEmpty()
            adapter.setList(it)
        }
    }

    override fun loadData() {
        super.loadData()
        showProgress()
        movieViewModels.getMovieList()
    }

}
