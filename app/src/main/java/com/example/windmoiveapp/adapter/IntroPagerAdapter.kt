package com.example.windmoiveapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.example.windmoiveapp.R
import com.example.windmoiveapp.extension.loadRoundedImage

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
                this.findViewById<TextView>(R.id.tvTitle).setText(R.string.qualityMovieLabel)
                //  this.tvContent.setText(R.string.startMessage0)
                this.findViewById<ImageView>(R.id.imvIntro).loadRoundedImage(R.drawable.spidermanbanner)

            }
            1 -> {
                this.findViewById<TextView>(R.id.tvTitle).setText(R.string.premiumAccountLabel)
                //this.findViewById<TextView>(R.id.tv).setText(R.string.startMessage1)
                this.findViewById<ImageView>(R.id.imvIntro).loadRoundedImage(R.drawable.monsterhighbanner)
            }
            2 -> {
                this.findViewById<TextView>(R.id.tvTitle).setText(R.string.hugeMovieStoreLabel)
                // this.findViewById<TextView>(R.id.tvTitle).setText(R.string.startMessage2)
                this.findViewById<ImageView>(R.id.imvIntro).loadRoundedImage(R.drawable.sangchibanner)
            }
            else -> {
                this.findViewById<TextView>(R.id.tvTitle).setText(R.string.fastestMovieNotifiLabel)
                //  this.findViewById<TextView>(R.id.tvTitle).setText(R.string.startMessage3)
                this.findViewById<ImageView>(R.id.imvIntro).loadRoundedImage(R.drawable.blackwidowbanner)

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