package com.example.windmoiveapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.windmoiveapp.R
import com.example.windmoiveapp.adapter.ViewPagerAdapter
import com.example.windmoiveapp.databinding.DashBoardScreenBinding
import com.example.windmoiveapp.extension.setFixedAdapter
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds


class DashBoardScreen() : BaseFragment<DashBoardScreenBinding>() {

    private val listFragment by lazy {
        arrayListOf(
            HomeFragment() as Fragment,
            NewAndHotFragment() as Fragment,
            DownloadingFragment.newInstance("", "") as Fragment
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(requireContext()) {}
    }

    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): DashBoardScreenBinding {
        return DashBoardScreenBinding.inflate(inflater)
    }

    override fun onViewInitialized(
        view: View,
        savedInstanceState: Bundle?,
        isViewCreated: Boolean
    ) {
        if (!isViewCreated) {
            initView()
            initListener()
            initObserver()
            val adRequest = AdRequest.Builder().build()
            binding.adView.loadAd(adRequest)
        }
    }

    private fun initObserver() {
    }

    private fun initListener() {

        binding.adView.adListener = object : AdListener() {
            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            override fun onAdClosed() {
                binding.adView.isVisible = false
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                // Code to be executed when an ad request fails.
            }

            override fun onAdImpression() {
                // Code to be executed when an impression is recorded
                // for an ad.
            }

            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }
        }
    }

    private fun initView() {
        setUpBottomNav()
    }

    private fun setUpBottomNav() {
        binding.apply {
            val viewPagerAdapter = ViewPagerAdapter(this@DashBoardScreen)
            viewPagerAdapter.setListFragment(listFragment)
            pagerDashBoard.setFixedAdapter(viewPagerAdapter)
            pagerDashBoard.isUserInputEnabled = false

            bottomNav.setOnItemSelectedListener { item ->
                setDefaultIcon()
                when (item.itemId) {
                    R.id.itemHome -> {
                        item.setIcon(R.drawable.ic_home_selected)
                        pagerDashBoard.setCurrentItem(HOME_SCREEN_POSITION, true)
                        return@setOnItemSelectedListener true
                    }
                    R.id.itemNewsHot -> {
                        item.setIcon(R.drawable.ic_new_hot_selected)
                        pagerDashBoard.setCurrentItem(NEWS_HOT_SCREEN_POSITION, true)
                        return@setOnItemSelectedListener true
                    }
                    R.id.itemDownLoad -> {
                        item.setIcon(R.drawable.ic_download_selected)
                        pagerDashBoard.setCurrentItem(DOWNLOAD_SCREEN_POSITION, true)
                        return@setOnItemSelectedListener true
                    }
                    else -> false
                }
            }
        }

    }

    private fun setDefaultIcon() {
        binding.bottomNav.menu.apply {
            val listIconDefault =
                listOf(R.drawable.ic_home, R.drawable.ic_new_hot, R.drawable.ic_round_download)
            listIconDefault.forEachIndexed { index, item ->
                getItem(index).setIcon(item)
            }
        }
    }

    companion object {
        const val HOME_SCREEN_POSITION = 0
        const val NEWS_HOT_SCREEN_POSITION = 1
        const val DOWNLOAD_SCREEN_POSITION = 2

        @JvmStatic
        fun newInstance() = DashBoardScreen()
    }

}