package com.example.windmoiveapp.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

open class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    protected var mListPagers = ArrayList<Fragment>()

    open fun setListFragment(list: ArrayList<Fragment>) {
        mListPagers = list
    }

    override fun getItemCount() = mListPagers.size

    override fun createFragment(position: Int): Fragment {
        return mListPagers[position]
    }

    fun getItem(position: Int): Fragment? {
        return mListPagers.getOrNull(position)
    }
}