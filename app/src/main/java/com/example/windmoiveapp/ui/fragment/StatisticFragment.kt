package com.example.windmoiveapp.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.windmoiveapp.R
import com.example.windmoiveapp.adapter.MovieChartAdapter
import com.example.windmoiveapp.constant.StatusLovingMovie
import com.example.windmoiveapp.databinding.FragmentStatisticBinding
import com.example.windmoiveapp.extension.click
import com.example.windmoiveapp.extension.configureUi
import com.example.windmoiveapp.extension.showCustomToast
import com.example.windmoiveapp.model.MovieModel
import com.example.windmoiveapp.model.dataLovingsPieEntry
import com.example.windmoiveapp.model.getNumberLoveMovies
import com.example.windmoiveapp.model.setBarDataNumberRatings
import com.example.windmoiveapp.viewmodels.MovieViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener

class StatisticFragment : BaseFragment<FragmentStatisticBinding>() {
    private val movieViewModels: MovieViewModel by viewModels()
    private val adapter: MovieChartAdapter by lazy { MovieChartAdapter() }
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                activity?.showCustomToast(getString(R.string.permissionGrantedLabel))
            } else {
                activity?.showCustomToast(getString(R.string.permissionDeniedLabel), false)
            }
        }
    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentStatisticBinding {
        return FragmentStatisticBinding.inflate(inflater, container, false)
    }

    override fun onViewInitialized(
        view: View,
        savedInstanceState: Bundle?,
        isViewCreated: Boolean
    ) {
        initView()
        initObserver()
        initListener()
    }

    override fun loadData() {
        super.loadData()
        showProgress()
        movieViewModels.getMovieList()
        movieViewModels.getRatingsList()
        movieViewModels.getLovingList()
    }

    private fun initView() {
        setUpBarChart()
        setUpPieChart()
        setUpRecyclerView()
    }

    private fun initListener() {
        binding.barChartMovie.setOnChartValueSelectedListener(object :
            OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                if (e == null) return
                movieViewModels.listMovieLiveData.value?.let {
                    navigateToMovieFragment(it[e.x.toInt()])
                }
            }

            override fun onNothingSelected() {}
        })

        binding.pieChartMovie.setOnChartValueSelectedListener(object :
            OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                if (e == null) return
                binding.rcvLoveMovies.isVisible = true
                if (h?.x?.toInt() == StatusLovingMovie.LIKE.status) {
                    setDataToLovingMovie(StatusLovingMovie.LIKE)
                } else if (h?.x?.toInt() == StatusLovingMovie.DISLIKE.status) {
                    setDataToLovingMovie(StatusLovingMovie.DISLIKE)
                } else{ }
            }

            override fun onNothingSelected() {}
        })
        binding.headerBar.apply {
            setEventBackListener {
                super.onBackFragment()
            }
        }
        binding.tvSaveBarchart.click {
            saveToGallery(binding.barChartMovie, "NumberRatingOnMovie")
        }
        binding.tvSavePieChart.click {
            saveToGallery(binding.pieChartMovie, "NumberLovingOnMovie")
        }
        adapter.onItemClick = {
            navigateToMovieFragment(it)
        }
    }


    private fun setUpRecyclerView() {
        binding.rcvLoveMovies.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            itemAnimator = DefaultItemAnimator()
            adapter = this@StatisticFragment.adapter
        }
    }

    private fun setUpPieChart() {
        binding.pieChartMovie.apply {
            legend.textColor = Color.WHITE
            animateY(1400, Easing.EaseInOutQuad)
        }
    }

    private fun initObserver() {
        movieViewModels.ratingsLiveData.observe(viewLifecycleOwner) {
            setDataToBarChart()
        }
        movieViewModels.listMovieLiveData.observe(viewLifecycleOwner) {
            setDataToBarChart()
        }
        movieViewModels.lovingsLiveData.observe(viewLifecycleOwner) {
            setDataToPieChart()
        }
    }

    private fun setDataToPieChart() {
        binding.pieChartMovie.data = dataLovingsPieEntry(movieViewModels.lovingsLiveData.value ?: emptyList())
        binding.pieChartMovie.invalidate()
    }

    private fun setUpBarChart() {
        binding.barChartMovie.configureUi()
    }


    private fun setDataToBarChart() {
        val movies = movieViewModels.listMovieLiveData.value
        val ratings = movieViewModels.ratingsLiveData.value
        if (movies != null && ratings != null) {
            dismissProgress()
            binding.barChartMovie.data = setBarDataNumberRatings(ratings, movies)
            binding.barChartMovie.invalidate()
        }
    }
    private fun setDataToLovingMovie(type: StatusLovingMovie) {
        val movies = movieViewModels.listMovieLiveData.value
        val lovings = movieViewModels.lovingsLiveData.value
        if (movies != null && lovings != null) {
            adapter.setList(getNumberLoveMovies(lovings, movies, type), type.status != StatusLovingMovie.DISLIKE.status)
        }
    }

    private fun saveToGallery(
        chart: Chart<*>,
        filename: String
    ) {
        if (getListPermissionDenied().contains(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            requestPermissionLauncher.launch(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        } else {
            if (chart.saveToGallery(filename + "_" + System.currentTimeMillis(), 70)) {
                activity.showCustomToast("Success")
            } else {
                activity.showCustomToast("Fail", true)
            }
        }
    }

    private fun navigateToMovieFragment(movieModel: MovieModel) {
        navigateToDestination(R.id.movieDetailFragment, bundleOf(MovieDetailFragment.BUNDLE_CONTENT_MOVIE to movieModel))
    }

}