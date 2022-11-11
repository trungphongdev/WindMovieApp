package com.example.windmoiveapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.windmoiveapp.R
import com.example.windmoiveapp.databinding.ItemCategoryBinding
import com.example.windmoiveapp.databinding.ItemMovieNormalBinding
import com.example.windmoiveapp.databinding.ItemMovieTypeCircleBinding
import com.example.windmoiveapp.databinding.ItemMovieTypeHighBinding
import com.example.windmoiveapp.databinding.LayoutItemListMovieBinding
import com.example.windmoiveapp.extension.loadImage
import com.example.windmoiveapp.model.MovieCategoryModel
import com.example.windmoiveapp.model.MovieModel

class MovieItemAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class MovieItemNormalViewHolder(var binding: ItemMovieNormalBinding) : RecyclerView.ViewHolder(binding.root)

    inner class MovieItemCircleViewHolder(var binding: ItemMovieTypeCircleBinding) : RecyclerView.ViewHolder(binding.root)

    inner class MovieItemHighViewHolder(var binding: ItemMovieTypeHighBinding) : RecyclerView.ViewHolder(binding.root)

    private lateinit var listMovie: ArrayList<MovieModel>

    private lateinit var movieCategoryModel: MovieCategoryModel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            MovieCategoryModel.TYPE_NORMAL -> {
                MovieItemNormalViewHolder(ItemMovieNormalBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            MovieCategoryModel.TYPE_CIRCLE -> {
                MovieItemCircleViewHolder(ItemMovieTypeCircleBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            else -> {
                MovieItemHighViewHolder(ItemMovieTypeHighBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemData = listMovie[position]
        when(holder) {
            is MovieItemNormalViewHolder -> {
                holder.binding.imgMovie.loadImage(itemData.movieUrl ?: "")
            }

            is MovieItemCircleViewHolder -> {
                holder.binding.imgMovie.loadImage(itemData.movieUrl ?: "")
                holder.binding.tvName.text = itemData.name
            }

            is MovieItemCircleViewHolder -> {
                holder.binding.imgMovie.loadImage(itemData.movieUrl ?: "")
            }
        }

    }

    override fun getItemCount() = listMovie.size

    override fun getItemViewType(position: Int): Int {
        return this.movieCategoryModel.viewType
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(movieCategoryModel: MovieCategoryModel) {
        this.movieCategoryModel = movieCategoryModel
        listMovie.clear()
        listMovie = movieCategoryModel.movies ?: arrayListOf()
        notifyDataSetChanged()
    }

}
