package com.example.windmoiveapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.windmoiveapp.R
import com.example.windmoiveapp.databinding.LayoutMessageNotificationBinding
import com.example.windmoiveapp.extension.click
import com.example.windmoiveapp.model.NotificationModel

class NotificationAdapter : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    inner class NotificationViewHolder(var binding: LayoutMessageNotificationBinding) :
        RecyclerView.ViewHolder(binding.root)

    private var listNotify: ArrayList<NotificationModel> = arrayListOf()

    // var onItemClickMovieItem: ((NotificationModel) -> Unit)? = null

    var onItemClickRemoveItem: ((NotificationModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        return NotificationViewHolder(
            LayoutMessageNotificationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val itemData = listNotify[position]
        if (itemData.img.isNullOrEmpty()) {
            holder.binding.imvNotify.setImageResource(R.drawable.logohome)
        } else {
            holder.binding.imvNotify.setImageURI(itemData.img.toUri())
        }
        holder.binding.tvContent.text = itemData.content
        holder.binding.tvTime.text = itemData.timeStamp.toString()

        holder.binding.imvMore.click {
            onItemClickRemoveItem?.invoke(itemData)
        }
    }

    override fun getItemCount(): Int {
        return listNotify.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<NotificationModel>) {
        listNotify.clear()
        listNotify.addAll(list)
        notifyDataSetChanged()
    }

}
