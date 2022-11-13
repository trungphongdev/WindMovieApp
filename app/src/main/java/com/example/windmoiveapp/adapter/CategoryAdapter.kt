package com.example.windmoiveapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.windmoiveapp.R
import com.example.windmoiveapp.constant.Categories
import com.example.windmoiveapp.databinding.ItemCategoryBinding

class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    private var listCategory: ArrayList<Categories> = arrayListOf()
    private var onItemClick: ((Categories) -> Unit)? = null
    private var selectedItemIndex = RecyclerView.NO_POSITION

    inner class CategoryViewHolder(var binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val itemBinding =
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(itemBinding)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val itemData = listCategory[position]
        val context = holder.binding.root.context
        holder.binding.tvCategory.text = itemData.type
        holder.itemView.setOnClickListener {
            if (position == RecyclerView.NO_POSITION) return@setOnClickListener
            selectedItemIndex = position
            notifyDataSetChanged()
            onItemClick?.invoke(itemData)
        }
        if (selectedItemIndex == position) {
            holder.binding.tvCategory.typeface =
                ResourcesCompat.getFont(context, R.font.roboto_bold)
            holder.binding.tvCategory.textSize = 22f
        } else {
            holder.binding.tvCategory.typeface =
                ResourcesCompat.getFont(context, R.font.roboto_regular)
            holder.binding.tvCategory.textSize = 16f
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
