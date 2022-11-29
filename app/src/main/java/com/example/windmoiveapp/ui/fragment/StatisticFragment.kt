package com.example.windmoiveapp.ui.fragment

import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.example.windmoiveapp.R
import com.example.windmoiveapp.databinding.FragmentStatisticBinding
import com.example.windmoiveapp.extension.click
import com.example.windmoiveapp.extension.showCustomToast
import com.example.windmoiveapp.model.ValueFormatterBarDataSet
import com.example.windmoiveapp.model.dataLovingsPieEntry
import com.example.windmoiveapp.model.setBarDataNumberRatings
import com.example.windmoiveapp.util.PERMISSION_REQUEST_CODE
import com.example.windmoiveapp.viewmodels.MovieViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener

class StatisticFragment : BaseFragment<FragmentStatisticBinding>() {
    private val movieViewModels: MovieViewModel by viewModels()
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Log.d("position", "position" + isGranted)

            } else {
                Log.d("position", "position" + false)
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

    private fun initListener() {
        binding.barChartMovie.setOnChartValueSelectedListener(object :
            OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                Log.d("position", "position" + e?.x)
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
            saveToGallery(binding.pieChartMovie, "NumberRatingOnMovie")
        }
    }

    private fun saveToGallery(
        chart: Chart<*>,
        name: String
    ) {
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        } else {
            if (chart.saveToGallery(name + "_" + System.currentTimeMillis(), 70)) {
                activity.showCustomToast("Success")
            } else {
                activity.showCustomToast("Fail", true)
            }
        }
    }


    private fun initView() {
        setUpBarChart()
        setUpPieChart()
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
        binding.barChartMovie.apply {
            description.isEnabled = false
            legend.textColor = Color.WHITE
            setDrawGridBackground(false)
            setPinchZoom(false)
            extraBottomOffset = 10F
            isDoubleTapToZoomEnabled = false
            animateY(2000)

            axisRight.apply {
                setDrawLabels(true)
                gridColor = Color.WHITE
                axisLineColor = Color.WHITE
                gridLineWidth = 1f
                axisLineWidth = 1f
                textColor = Color.WHITE
                valueFormatter = ValueFormatterBarDataSet()
            }

            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                textColor = Color.WHITE
                setDrawGridLines(false)
                setDrawGridLines(false)
                setDrawAxisLine(true)
                setDrawLabels(true)
                granularity = 1f
                spaceMin = 1F
                xOffset = 20f
            }
        }
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

    override fun loadData() {
        super.loadData()
        showProgress()
        movieViewModels.getMovieList()
        movieViewModels.getRatingsList()
        movieViewModels.getLovingList()
    }

}