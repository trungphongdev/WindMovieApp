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
import com.example.windmoiveapp.adapter.MyListMovieAdapter
import com.example.windmoiveapp.databinding.FragmentMyListMovieBinding
import com.example.windmoiveapp.extension.navigateWithAnim
import com.example.windmoiveapp.extension.showAlertDialog
import com.example.windmoiveapp.viewmodels.MovieViewModel


class MyListFragment : BaseFragment<FragmentMyListMovieBinding>() {
    private val adapter: MyListMovieAdapter by lazy { MyListMovieAdapter() }
    private val movieViewModels: MovieViewModel by lazy { MovieViewModel(activity?.application as Application) }

    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentMyListMovieBinding {
        return FragmentMyListMovieBinding.inflate(inflater, container, false)
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

        adapter.onItemClickRemoveItem = {
            activity?.showAlertDialog(getString(R.string.removeMovieLabel, it.name),
                isShowNegativeBtn = true,
                cancelListener = {

                },
                callBack = {
                    movieViewModels.removeMovieById(it)
                    movieViewModels.getListMovieRoom()
                })
        }

        binding.headerBar.apply {
            setEventBackListener {
                onBackFragment()
            }
        }
    }

    private fun initObserver() {
        movieViewModels.listMovieRoom.observe(viewLifecycleOwner) {
            dismissProgress()
            if (it.isNullOrEmpty()) {
                binding.llEmptyData.root.isVisible = true
                binding.rcvMovies.isGone = true
            } else {
                adapter.setList(it)
                binding.llEmptyData.root.isGone = true
                binding.rcvMovies.isVisible = true
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
        movieViewModels.getListMovieRoom()
    }
}
