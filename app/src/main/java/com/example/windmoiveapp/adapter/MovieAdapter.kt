package com.example.windmoiveapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.windmoiveapp.databinding.LayoutItemListMovieBinding
import com.example.windmoiveapp.model.MovieCategoryModel
import com.example.windmoiveapp.model.MovieModel

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(var binding: LayoutItemListMovieBinding) :
        RecyclerView.ViewHolder(binding.root)

    private var listMovie: ArrayList<MovieCategoryModel> = arrayListOf()

    var onItemClickMovie: ((MovieModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val itemBinding =
            LayoutItemListMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val itemData = listMovie[position]
        holder.binding.tvCategory.text = itemData.category

        val itemMovieHzAdapter = MovieItemAdapter()
        itemMovieHzAdapter.setList(itemData)
        holder.binding.rcvMovies.apply {
            adapter = itemMovieHzAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
        itemMovieHzAdapter.onItemClickMovieItem = {
            onItemClickMovie?.invoke(it)
        }
    }

    override fun getItemCount() = listMovie.size

    @SuppressLint("NotifyDataSetChanged")
    fun setList(arrayList: List<MovieCategoryModel>) {
        listMovie.clear()
        listMovie.addAll(arrayList)
        notifyDataSetChanged()
    }

}
