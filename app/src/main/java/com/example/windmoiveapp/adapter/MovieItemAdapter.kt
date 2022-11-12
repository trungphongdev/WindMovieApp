package com.example.windmoiveapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.windmoiveapp.databinding.ItemMovieNormalBinding
import com.example.windmoiveapp.databinding.ItemMovieTypeCircleBinding
import com.example.windmoiveapp.databinding.ItemMovieTypeHighBinding
import com.example.windmoiveapp.extension.click
import com.example.windmoiveapp.extension.loadImage
import com.example.windmoiveapp.model.MovieCategoryModel
import com.example.windmoiveapp.model.MovieModel

class MovieItemAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class MovieItemNormalViewHolder(var binding: ItemMovieNormalBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class MovieItemCircleViewHolder(var binding: ItemMovieTypeCircleBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class MovieItemHighViewHolder(var binding: ItemMovieTypeHighBinding) :
        RecyclerView.ViewHolder(binding.root)

    private var listMovie: ArrayList<MovieModel> = arrayListOf()

    private lateinit var movieCategoryModel: MovieCategoryModel

    var onItemClickMovieItem: ((MovieModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            MovieCategoryModel.TYPE_NORMAL -> {
                MovieItemNormalViewHolder(
                    ItemMovieNormalBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            MovieCategoryModel.TYPE_CIRCLE -> {
                MovieItemCircleViewHolder(
                    ItemMovieTypeCircleBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    )
                )
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
                holder.binding.imgMovie.loadImage(itemData.image ?: "")
                holder.binding.root.click {
                    onItemClickMovieItem?.invoke(itemData)
                }
            }

            is MovieItemCircleViewHolder -> {
                holder.binding.imgMovie.loadImage(itemData.image ?: "")
                holder.binding.tvName.text = itemData.name
                holder.binding.root.click {
                    onItemClickMovieItem?.invoke(itemData)
                }
            }

            is MovieItemHighViewHolder -> {
                holder.binding.imgMovie.loadImage(itemData.image ?: "")
                holder.binding.root.click {
                    onItemClickMovieItem?.invoke(itemData)
                }
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
        listMovie.addAll(movieCategoryModel.movies?.shuffled() ?: arrayListOf())
        notifyDataSetChanged()
    }

}
