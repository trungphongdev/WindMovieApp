package com.example.windmoiveapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.windmoiveapp.constant.Categories
import com.example.windmoiveapp.databinding.ItemCategoryBinding
import com.example.windmoiveapp.extension.click

class BottomSheetAdapter : RecyclerView.Adapter<BottomSheetAdapter.CategoryViewHolder>() {
    private var items: ArrayList<Any> = arrayListOf()
    var onItemClick: ((Int) -> Unit)? = null

    inner class CategoryViewHolder(var binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val itemBinding =
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val item = items[position]
        holder.binding.tvCategory.text = item.toString()
        holder.itemView.click {
            onItemClick?.invoke(position)
        }
    }

    override fun getItemCount() = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<Any>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

}
