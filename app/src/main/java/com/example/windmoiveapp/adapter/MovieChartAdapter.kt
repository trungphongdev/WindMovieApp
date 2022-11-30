package com.example.windmoiveapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.windmoiveapp.R
import com.example.windmoiveapp.databinding.ItemLoveMovieBinding
import com.example.windmoiveapp.databinding.LayoutItemListMovieBinding
import com.example.windmoiveapp.extension.click
import com.example.windmoiveapp.extension.loadImage
import com.example.windmoiveapp.model.MovieCategoryModel
import com.example.windmoiveapp.model.MovieModel
import com.example.windmoiveapp.model.charts.MovieChartModel

class MovieChartAdapter : RecyclerView.Adapter<MovieChartAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(var binding: ItemLoveMovieBinding) :
        RecyclerView.ViewHolder(binding.root)

    private var items: ArrayList<MovieChartModel> = arrayListOf()

    var onItemClick:((MovieModel) -> Unit)? = null

    private var isLove: Boolean = true


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val itemBinding =
            ItemLoveMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val item = items[position]
        holder.binding.tvNameMovie.text = item.movieModel.name
        holder.binding.tvNumberLove.text = item.number.toString()
        holder.binding.imvMovie.loadImage(item.movieModel.image ?: "")
        if (isLove) {
            holder.binding.tvNumberLove.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_love, 0, 0)
        } else {
            holder.binding.tvNumberLove.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.dislike, 0, 0)
        }
        holder.binding.root.click {
            onItemClick?.invoke(item.movieModel)
        }
    }

    override fun getItemCount() = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<MovieChartModel>, _isLove: Boolean = true) {
        isLove = _isLove
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

}
