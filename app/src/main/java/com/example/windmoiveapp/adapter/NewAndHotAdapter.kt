package com.example.windmoiveapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.windmoiveapp.R
import com.example.windmoiveapp.databinding.ItemLayoutNewHotBinding
import com.example.windmoiveapp.extension.getDateRandom
import com.example.windmoiveapp.extension.loadImage
import com.example.windmoiveapp.model.MovieModel

class NewAndHotAdapter : RecyclerView.Adapter<NewAndHotAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(var binding: ItemLayoutNewHotBinding) :
        RecyclerView.ViewHolder(binding.root)

    private var listMovie: ArrayList<MovieModel> = arrayListOf()

    var onItemClickMovieItem: ((MovieModel) -> Unit)? = null

    var onItemClickInfoItem: ((MovieModel) -> Unit)? = null

    var onItemClickFavouriteItem: ((Boolean, MovieModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            ItemLayoutNewHotBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val itemData = listMovie[position]
        holder.binding.tvTimeRelease.text = getDateRandom()
        holder.binding.imvMovie.loadImage(itemData.image ?: "", radiusId = R.dimen.dp12)
        holder.binding.tvNameMovie.text = itemData.name
        holder.binding.tvDescription.text = itemData.description
        //holder.binding.tvNumberLike.text = itemData.likeNum.toString()
        holder.binding.root.setOnClickListener {
            onItemClickMovieItem?.invoke(itemData)
        }
        holder.binding.imvInfo.setOnClickListener {

            onItemClickInfoItem?.invoke(itemData)
        }
        holder.binding.imvLove.setOnClickListener {
            onItemClickFavouriteItem?.invoke(true, itemData)
            holder.binding.imvLove.setImageResource(R.drawable.ic_love)
        }

        holder.binding.tvDescription.setOnClickListener {
            if (holder.binding.tvDescription.lineCount > 1) {
                holder.binding.tvDescription.isSingleLine = true
                holder.binding.tvDescription.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_drop_up,
                    0
                )
            } else {
                holder.binding.tvDescription.isSingleLine = false
                holder.binding.tvDescription.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_arrow_drop_down,
                    0
                )
            }

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
