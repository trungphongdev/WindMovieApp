package com.example.windmoiveapp.ui.fragment

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.windmoiveapp.R
import com.example.windmoiveapp.adapter.CategoryAdapter
import com.example.windmoiveapp.adapter.MovieAdapter
import com.example.windmoiveapp.constant.Categories
import com.example.windmoiveapp.databinding.FragmentHomeBinding
import com.example.windmoiveapp.extension.click
import com.example.windmoiveapp.extension.navigateWithAnim
import com.example.windmoiveapp.model.MovieCategoryModel
import com.example.windmoiveapp.model.MovieModel
import com.example.windmoiveapp.model.setListMovieByType
import com.example.windmoiveapp.util.PERMISSION_REQUEST_CODE
import com.example.windmoiveapp.viewmodels.MovieViewModel
import kotlin.random.Random


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
        initViews()
        initListener()
        initObserver()
    }

    private fun initListener() {
        binding.tvCategories.click {
            binding.llCategories.isVisible = true
        }
        binding.imvCloseCategory.click {
            binding.llCategories.isVisible = false
        }

        categoryAdapter.setOnItemClickCategory {
            binding.llCategories.isVisible = false
            binding.tvCategories.text = it.type
            if (it.name == Categories.HOME.name) {
                binding.tvCategories.text = getString(R.string.categoriesLabel)
            }
            callDataListMovieByCategory(it)
        }

        categoryMovieAdapter.onItemClickMovie = {
            showBottomSheetMovieDetail(it)
        }
        binding.headerBar.apply {
            setEventSearchListener {
                findNavController().navigateWithAnim(R.id.searchFragment)
            }
            setEventAccountListener {
                findNavController().navigateWithAnim(R.id.tabMeFragment)
            }
            setEventBackListener {
                mainActivity?.showDialogBackPress()
            }
        }

        binding.rcvMovies.addOnScrollListener(object  : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                binding.fabRandomMovie.isVisible = dy > 0 && binding.llCategories.isVisible == false
            }
        })

        binding.fabRandomMovie.click {
            val movies = movieViewModels.listMovieLiveData.value
            if (movies.isNullOrEmpty().not()) {
                findNavController().navigateWithAnim(
                    R.id.movieDetailFragment,
                    bundleOf(MovieDetailFragment.BUNDLE_CONTENT_MOVIE to (movies?.get(Random.nextInt(movies.size))))
                )

            }
        }

    }

    private fun showBottomSheetMovieDetail(movieModel: MovieModel) {
        MovieDetailBottomSheet.newInstance(movieModel)
            .show(childFragmentManager, MovieDetailBottomSheet.TAG)
    }

    private fun initObserver() {
        movieViewModels.listMovieLiveData.observe(viewLifecycleOwner) {
            dismissProgress()
            movieViewModels.convertToListMovieByCategory(it)
        }
        movieViewModels.listMovieByCategories.observe(viewLifecycleOwner) {
            dismissProgress()
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
        requestPermissionManageAllFileAndroid11()
    }

     private fun requestPermissionManageAllFileAndroid11(): Boolean {
        if (Build.VERSION.SDK_INT >= 30) {
            if (!Environment.isExternalStorageManager()) {
                    Intent().apply {
                        action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                        startActivity(this)
                    }
                return false
            }
        }
        return true
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

    private fun callDataListMovieByCategory(it: Categories) {
        showProgress()
        if (it.name == Categories.HOME.name) {
            movieViewModels.getMovieList()
        } else {
            // movieViewModels.getMovieListByCategory(it)
            movieViewModels.getListMovieByCategory(it)
        }
    }

    private fun setDataForAdapterListMovieCategory(it: List<MovieCategoryModel>) {
        categoryMovieAdapter.setList(it.setListMovieByType())
    }

}
