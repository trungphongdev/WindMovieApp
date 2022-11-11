package com.example.windmoiveapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.windmoiveapp.R
import com.example.windmoiveapp.constant.Categories
import com.example.windmoiveapp.databinding.ItemCategoryBinding

class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    private var listCategory: ArrayList<Categories> = arrayListOf()
    private var onItemClick:((Categories) -> Unit)? = null
    private var selectedItemIndex = 0
    inner class CategoryViewHolder(var binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val itemBinding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val itemData = listCategory[position]
        holder.binding.tvCategory.text = itemData.type
        holder.itemView.setOnClickListener {
            selectedItemIndex = position
            notifyItemChanged(selectedItemIndex)
            holder.binding.tvCategory.apply {
                if (selectedItemIndex == position) {
                    typeface = ResourcesCompat.getFont(context, R.font.roboto_bold)
                    textSize = 20f
                } else {
                    typeface = ResourcesCompat.getFont(context, R.font.roboto_regular)
                    textSize = 16f
                }
            }

            onItemClick?.invoke(itemData)
        }
        if (position == listCategory.size -1) {
            holder.binding.tvCategory.setPadding(0,0,0,40)
        }
    }

    override fun getItemCount() = listCategory.size

    @SuppressLint("NotifyDataSetChanged")
    fun setList(arrayList: List<Categories>) {
        listCategory.clear()
        listCategory.addAll(arrayList)
        notifyDataSetChanged()
    }

    fun setOnItemClickCategory(_onclick:((Categories) ->Unit)? = null) {
        onItemClick = _onclick
    }

}
