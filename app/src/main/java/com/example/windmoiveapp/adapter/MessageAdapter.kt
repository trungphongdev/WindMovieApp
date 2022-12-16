package com.example.windmoiveapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.windmoiveapp.databinding.ItemConversationBinding
import com.example.windmoiveapp.extension.loadCircleImage
import com.example.windmoiveapp.model.UserModel

class MessageAdapter : RecyclerView.Adapter<MessageAdapter.ViewHolder>() {
    var users = ArrayList<UserModel>()

    var onItemClick: ((UserModel) -> Unit)? = null

    inner class ViewHolder(var binding: ItemConversationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserModel) {
            binding.imvUserChat.loadCircleImage(user.photoUrl)
            binding.tvUserId.text = user.uid
            binding.root.setOnClickListener {
                onItemClick?.invoke(user)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemConversationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = users[position]
        holder.bind(user)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(users: List<UserModel>) {
        this.users.clear()
        this.users.addAll(users)
        notifyDataSetChanged()
    }
}