package com.example.windmoiveapp.ui.fragment

import android.app.Application
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.windmoiveapp.R
import com.example.windmoiveapp.adapter.CategoryAdapter
import com.example.windmoiveapp.adapter.MovieAdapter
import com.example.windmoiveapp.constant.Categories
import com.example.windmoiveapp.databinding.FragmentHomeBinding
import com.example.windmoiveapp.extension.click
import com.example.windmoiveapp.model.MovieCategoryModel
import com.example.windmoiveapp.model.MovieModel
import com.example.windmoiveapp.util.PERMISSION_REQUEST_CODE
import com.example.windmoiveapp.viewmodels.MovieViewModel


class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private val categoryAdapter: CategoryAdapter by lazy { CategoryAdapter() }
    private val categoryMovieAdapter: MovieAdapter by lazy { MovieAdapter() }
    private val movieViewModels: MovieViewModel by lazy { MovieViewModel(activity?.application as Application) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermission()
    }

    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    override fun onViewInitialized(
        view: View,
        savedInstanceState: Bundle?,
        isViewCreated: Boolean
    ) {
        //movieViewModels.getMovieList()
        initViews()
        initListener()
        initObserver()
    }

    private fun initListener() {
        binding.tvCategories.click {
            binding.rcvCategories.isVisible = true
            binding.imvCloseCategory.isVisible = true
        }
        binding.imvCloseCategory.click {
            binding.rcvCategories.isGone = true
            binding.imvCloseCategory.isGone = true
        }

        categoryAdapter.setOnItemClickCategory {
            binding.rcvCategories.isGone = true
            binding.imvCloseCategory.isGone = true
            binding.tvCategories.text = it.type
            if (it.name == Categories.HOME.name) {
                binding.tvCategories.text = getString(R.string.categoriesLabel)
            }
        }

        categoryMovieAdapter.onItemClickMovie = {
            showBottomSheetMovieDetail(it)
        }
    }

    private fun showBottomSheetMovieDetail(movieModel: MovieModel) {
        MovieDetailBottomSheet.newInstance(movieModel)
            .show(childFragmentManager, MovieDetailBottomSheet.TAG)
    }

    private fun initObserver() {
        movieViewModels.listMovie.observe(viewLifecycleOwner) {
            dismissProgress()
            movieViewModels.convertToListMovieByCategory(it)
            Log.d("listOfMovie1", "" + it.size)
        }
        movieViewModels.listMovieByCategories.observe(viewLifecycleOwner) {
            Log.d("listOfMovie2", "" + it.size)
            setDataForAdapterListMovieCategory(it ?: emptyList())
        }
    }

    private fun initViews() {
        setUpRecyclerViewCategory()
        setUpRecyclerViewCategoryMovie()
    }

    private fun setUpRecyclerViewCategoryMovie() {
        binding.apply {
            rcvMovies.adapter = categoryMovieAdapter
            rcvMovies.itemAnimator = DefaultItemAnimator()
            rcvMovies.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        }
    }

    private fun setUpRecyclerViewCategory() {
        binding.apply {
            categoryAdapter.setList(Categories.values().toList())
            rcvCategories.adapter = categoryAdapter
            rcvCategories.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun requestPermission() {
        if (requestMultiPermission()) {
            activity?.requestPermissions(listPermission.toTypedArray(), PERMISSION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            when (requestCode) {
                PERMISSION_REQUEST_CODE -> {
                    Toast.makeText(context ?: return, getString(R.string.permissionGrantedLabel), Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(
                context ?: return,
                getString(R.string.permissionDeniedLabel),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun loadData() {
        super.loadData()
        showProgress()
        movieViewModels.getMovieList()
    }

    private fun setDataForAdapterListMovieCategory(it: List<MovieCategoryModel>) {
        categoryMovieAdapter.setList(it)
    }


}
