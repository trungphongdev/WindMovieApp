package com.example.windmoiveapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.windmoiveapp.databinding.ItemMovieNormalBinding
import com.example.windmoiveapp.extension.click
import com.example.windmoiveapp.extension.loadImage
import com.example.windmoiveapp.model.MovieModel

class MovieTrailerAdapter : RecyclerView.Adapter<MovieTrailerAdapter.MovieItemNormalViewHolder>() {

    inner class MovieItemNormalViewHolder(var binding: ItemMovieNormalBinding) :
        RecyclerView.ViewHolder(binding.root)

    private var listMovie: ArrayList<MovieModel> = arrayListOf()

    var onItemClickMovieItem: ((MovieModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieItemNormalViewHolder {
        return MovieItemNormalViewHolder(
            ItemMovieNormalBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MovieItemNormalViewHolder, position: Int) {
        val itemData = listMovie[position]
        holder.binding.imgMovie.loadImage(itemData.image ?: "")
        holder.binding.root.click {
            onItemClickMovieItem?.invoke(itemData)
        }
    }

    override fun getItemCount(): Int {
        return listMovie.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<MovieModel>) {
        listMovie.clear()
        listMovie.addAll(list)
        notifyDataSetChanged()
    }

}
