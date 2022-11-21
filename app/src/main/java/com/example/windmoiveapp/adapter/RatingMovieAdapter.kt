package com.example.windmoiveapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.windmoiveapp.databinding.ItemMyListBinding
import com.example.windmoiveapp.databinding.LayoutItemCommentBinding
import com.example.windmoiveapp.extension.click
import com.example.windmoiveapp.extension.loadCircleImage
import com.example.windmoiveapp.extension.loadImage
import com.example.windmoiveapp.model.MovieModel
import com.example.windmoiveapp.model.RatingModel
import com.example.windmoiveapp.util.PATTERN_TIME
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class RatingMovieAdapter : RecyclerView.Adapter<RatingMovieAdapter.CommentMovieViewHolder>() {

    inner class CommentMovieViewHolder(var binding: LayoutItemCommentBinding) :
        RecyclerView.ViewHolder(binding.root)

    private var ratings: ArrayList<RatingModel> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentMovieViewHolder {
        return CommentMovieViewHolder(
            LayoutItemCommentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CommentMovieViewHolder, position: Int) {
        val rate = ratings[position]
        holder.binding.imvUser.loadCircleImage(rate.userModel?.photoUrl)
        holder.binding.tvUsername.text = rate.userModel?.name ?: ""
        holder.binding.tvTime.text = SimpleDateFormat(PATTERN_TIME, Locale.getDefault()).format(rate.time)
        holder.binding.tvComment.text = rate.comment ?: ""
    }

    override fun getItemCount(): Int {
        return ratings.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<RatingModel>) {
        ratings.clear()
        ratings.addAll(list)
        notifyDataSetChanged()
    }

}
