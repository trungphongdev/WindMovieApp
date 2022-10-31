package com.example.windmoiveapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.example.windmoiveapp.R

class IntroPagerAdapter(private val context: Context) : PagerAdapter() {

    companion object {
        const val PAGE_COUNT = 4
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(context).inflate(R.layout.item_intro, container, false)
        view.bindViews(position)
        container.addView(view)
        return view
    }

    private fun View.bindViews(position: Int) {
        when (position) {
            0 -> {
                this.tvTitle.setText(R.string.startTitle1)
                this.tvContent.setText(R.string.startMessage0)
                this.imvIntro.loadImage(R.drawable.intro_0_new)
            }
            1 -> {
                this.tvTitle.setText(R.string.startTitle1)
                this.tvContent.setText(R.string.startMessage1)
                this.imvIntro.loadImage(R.drawable.ic_intro_new_2)
            }
            2 -> {
                this.tvTitle.setText(R.string.startTitle2)
                this.tvContent.setText(R.string.startMessage2)
                this.imvIntro.loadImage(R.drawable.intro_2_no_ft)
            }
            else -> {
                this.tvTitle.setText(R.string.startTitle3)
                this.tvContent.setText(R.string.startMessage3)
                this.imvIntro.loadImage(R.drawable.intro_3_new)
            }
        }
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }

    override fun getCount(): Int = PAGE_COUNT

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }
}