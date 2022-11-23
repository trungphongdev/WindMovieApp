package com.example.windmoiveapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.windmoiveapp.databinding.ItemMovieBinding
import com.example.windmoiveapp.databinding.ItemMyListBinding
import com.example.windmoiveapp.databinding.ItemUserBinding
import com.example.windmoiveapp.extension.click
import com.example.windmoiveapp.extension.loadImage
import com.example.windmoiveapp.model.MovieModel
import com.example.windmoiveapp.model.UserModel

class ManagerMoviesAdapter : RecyclerView.Adapter<ManagerMoviesAdapter.MovieViewHolder>() {
    companion object {
        const val INFO_MOVIE = 0
        const val ADD_MOVIE = 1
        const val REMOVE_MOVIE = 2
        const val EDIT_MOVIE = 3
    }

    inner class MovieViewHolder(var binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root)

    private var movies: ArrayList<MovieModel> = arrayListOf()

    var onItemClick: ((MovieModel, Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            ItemMovieBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.binding.imgMovie.loadImage(movie.image ?: "")
        holder.binding.tvUid.text = movie.id
        holder.binding.root.click {
            onItemClick?.invoke(movie, INFO_MOVIE)
        }
        holder.binding.imvTrash.click {
            onItemClick?.invoke(movie, REMOVE_MOVIE)
        }
        holder.binding.imvEdit.click {
            onItemClick?.invoke(movie, EDIT_MOVIE)
        }
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<MovieModel>) {
        movies.clear()
        movies.addAll(list)
        notifyDataSetChanged()
    }

}
