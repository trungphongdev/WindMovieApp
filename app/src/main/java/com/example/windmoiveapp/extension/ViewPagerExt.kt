package com.example.windmoiveapp.extension

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.windmoiveapp.adapter.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import timber.log.Timber


fun ViewPager2.setFixedAdapter(adapter: FragmentStateAdapter) {
    try {
        this.adapter = adapter
    } catch (e: Exception) {
        Timber.e(e)
    }
}

fun ViewPager2.setupViewPager(
    currentFragment: Fragment,
    tabLayout: TabLayout,
    fragmentList: ArrayList<Fragment>,
    titleTabList: ArrayList<String>
) {
    ViewPagerAdapter(currentFragment).apply {
        setListFragment(fragmentList)
        setFixedAdapter(this)
        isUserInputEnabled = false
    }
    TabLayoutMediator(tabLayout, this) { tab, position ->
        tab.text = titleTabList[position]
    }.attach()
}

fun ViewPager2.onPageSelected(callback: (Int) -> Unit) {
    registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            callback(position)
        }
    })
}