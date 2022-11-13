package com.example.windmoiveapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.windmoiveapp.databinding.ItemMyListBinding
import com.example.windmoiveapp.extension.click
import com.example.windmoiveapp.extension.loadImage
import com.example.windmoiveapp.model.MovieModel

class MyListMovieAdapter : RecyclerView.Adapter<MyListMovieAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(var binding: ItemMyListBinding) :
        RecyclerView.ViewHolder(binding.root)

    private var listMovie: ArrayList<MovieModel> = arrayListOf()

    var onItemClickMovieItem: ((MovieModel) -> Unit)? = null

    var onItemClickRemoveItem: ((MovieModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            ItemMyListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val itemData = listMovie[position]
        holder.binding.imgMovie.loadImage(itemData.image ?: "")
        holder.binding.tvName.text = itemData.name
        holder.binding.tvDescription.text = itemData.description
        holder.binding.root.click {
            onItemClickMovieItem?.invoke(itemData)
        }
        holder.binding.imvTrash.click {
            onItemClickRemoveItem?.invoke(itemData)
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
