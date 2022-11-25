package com.example.windmoiveapp.ui.fragment

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.windmoiveapp.R
import com.example.windmoiveapp.adapter.MovieItemMoreLikeThisAdapter
import com.example.windmoiveapp.adapter.TopSearchAdapter
import com.example.windmoiveapp.databinding.FragmentSearchMovieBinding
import com.example.windmoiveapp.extension.afterTextChange
import com.example.windmoiveapp.extension.navigateWithAnim
import com.example.windmoiveapp.model.MovieModel
import com.example.windmoiveapp.viewmodels.MovieViewModel

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SearchMovieFragment : BaseFragment<FragmentSearchMovieBinding>() {
    private val adapterSearch: MovieItemMoreLikeThisAdapter by lazy { MovieItemMoreLikeThisAdapter() }
    private val adapterTopSearch: TopSearchAdapter by lazy { TopSearchAdapter() }
    private val movieViewModels: MovieViewModel by lazy { MovieViewModel(activity?.application as Application) }

    private var param1: String? = null
    private var param2: String? = null

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchMovieFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentSearchMovieBinding {
        return FragmentSearchMovieBinding.inflate(inflater, container, false)
    }

    override fun onViewInitialized(
        view: View,
        savedInstanceState: Bundle?,
        isViewCreated: Boolean
    ) {
        initViews()
        initListener()
        initObservers()
    }

    private fun initObservers() {
        movieViewModels.listMovieLiveData.observe(viewLifecycleOwner) {
            adapterTopSearch.setList(it)
        }
        movieViewModels.listMovieByNameLiveData.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                binding.rcvTopSearch.isVisible = true
                binding.rcvSearch.isVisible = false
            } else {
                adapterSearch.setList(it)
                binding.rcvTopSearch.isVisible = false
                binding.rcvSearch.isVisible = true
            }
        }
    }

    private fun initViews() {
        setUpRecyclerViewTopSearch()
        setUpRecyclerViewSearch()
    }

    private fun setUpRecyclerViewTopSearch() {
        binding.rcvTopSearch.adapter = adapterTopSearch
        binding.rcvTopSearch.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rcvTopSearch.itemAnimator = DefaultItemAnimator()
    }

    private fun setUpRecyclerViewSearch() {
        binding.rcvSearch.adapter = adapterSearch
        binding.rcvSearch.layoutManager = GridLayoutManager(context, 3)
        binding.rcvSearch.itemAnimator = DefaultItemAnimator()
    }

    private fun initListener() {
        binding.edtSearch.afterTextChange {
            if (it.trim().isBlank()) {
                binding.rcvTopSearch.isVisible = true
                binding.rcvSearch.isVisible = false
            } else {
                movieViewModels.getMovieListByName(it.toString())
                binding.rcvTopSearch.isVisible = false
                binding.rcvSearch.isVisible = true
            }
        }
        adapterSearch.onItemClickMovieItem = {
            navigateToDetailScreen(it)
        }
        adapterTopSearch.onItemClickMovieItem = {
            navigateToDetailScreen(it)
        }
        binding.imvRemoveSearch.setOnClickListener {
            binding.rcvTopSearch.isVisible = true
            binding.rcvSearch.isVisible = false
        }
        binding.headerBarSearch.setEventBackListener {
            onBackFragment()
        }

    }

    override fun loadData() {
        super.loadData()
        movieViewModels.getMovieList()
    }

    private fun navigateToDetailScreen(movieModel: MovieModel) {
        findNavController().navigateWithAnim(
            R.id.movieDetailFragment, bundleOf(
                MovieDetailFragment.BUNDLE_CONTENT_MOVIE to movieModel
            )
        )
    }


}