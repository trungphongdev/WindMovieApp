package com.example.windmoiveapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.windmoiveapp.R
import com.example.windmoiveapp.adapter.ViewPagerAdapter
import com.example.windmoiveapp.databinding.DashBoardScreenBinding
import com.example.windmoiveapp.extension.setFixedAdapter

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class DashBoardScreen() : BaseFragment<DashBoardScreenBinding>() {

    private var param1: String? = null
    private var param2: String? = null

    private val listFragment by lazy {
        arrayListOf(
            HomeFragment.newInstance("", "") as Fragment,
            NewsFragment.newInstance("", "") as Fragment,
            DownloadingFragment.newInstance("", "") as Fragment
        )
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
        }
    }

    private fun initObserver() {
        TODO("Not yet implemented")
    }

    private fun initListener() {
        TODO("Not yet implemented")
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