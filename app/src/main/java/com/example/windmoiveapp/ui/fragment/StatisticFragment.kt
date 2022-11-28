package com.example.windmoiveapp.ui.fragment

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.windmoiveapp.R
import com.example.windmoiveapp.databinding.FragmentInfoUserBinding
import com.example.windmoiveapp.databinding.FragmentStatisticBinding
import com.example.windmoiveapp.model.charts.barDataChart
import com.example.windmoiveapp.viewmodels.MovieViewModel


class StatisticFragment : BaseFragment<FragmentStatisticBinding>() {
    private val movieViewModels: MovieViewModel by viewModels()

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
        initObserver()
    }

    private fun initObserver() {
      movieViewModels.ratingsLiveData.observe(viewLifecycleOwner) {
          if (it != null  && movieViewModels.listMovieLiveData.value != null) {
              binding.barChartMovie.data = barDataChart( movieViewModels.listMovieLiveData.value, it)

          }
      }
        movieViewModels.listMovieLiveData.observe(viewLifecycleOwner) {
            if (it != null  && movieViewModels.ratingsLiveData.value != null) {
                binding.barChartMovie.data = barDataChart()

            }
        }
    }

    override fun loadData() {
        super.loadData()
        movieViewModels.getMovieList()
        movieViewModels.getRatingsList()
    }

}