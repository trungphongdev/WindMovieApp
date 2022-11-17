package com.example.windmoiveapp.adapter


import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import com.example.windmoiveapp.R
import com.example.windmoiveapp.databinding.ItemCategoryDropDownBinding

class CategoryArrayAdapter(context: Context, list: List<String>, isTextCenter: Boolean = true) :
    ArrayAdapter<String>(context, 0, list) {
    private var index = 0
    private val mIsTextCenter = isTextCenter

    fun setChoose(index: Int) {
        this.index = index
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initViewDropDown(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initViewDropDown(position, convertView, parent)
    }

    private fun initViewDropDown(position: Int, convertView: View?, parent: ViewGroup): View {
        val item = getItem(position)
        val view = ItemCategoryDropDownBinding.inflate(
            (parent.context as Activity).layoutInflater,
            parent,
            false
        )
        view.tvCategory.apply {
            text = item
            gravity = if (mIsTextCenter) Gravity.CENTER_HORIZONTAL else Gravity.START
        }
        view.tvCategory.apply {
            text = item
            setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    if (index == position) android.R.color.holo_red_light else R.color.white
                )
            )
            setTextColor(ContextCompat.getColor(
                context,
                if (index == position) R.color.white else android.R.color.holo_red_light
            ))
        }
        return view.root
    }
}