package com.example.windmoiveapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.windmoiveapp.databinding.ItemUserBinding
import com.example.windmoiveapp.extension.click
import com.example.windmoiveapp.extension.loadImage
import com.example.windmoiveapp.model.UserModel
import com.example.windmoiveapp.util.EDIT_USER
import com.example.windmoiveapp.util.INFO_USER
import com.example.windmoiveapp.util.REMOVE_USER

class ManagerUsersAdapter : RecyclerView.Adapter<ManagerUsersAdapter.UserViewHolder>() {

    inner class UserViewHolder(var binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root)

    private var users: ArrayList<UserModel> = arrayListOf()

    var onItemClick: ((UserModel, Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            ItemUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.binding.imgUser.loadImage(user.photoUrl)
        holder.binding.tvUid.text = user.uid
        holder.binding.root.click {
            onItemClick?.invoke(user, INFO_USER)
        }
        holder.binding.imvTrash.click {
            onItemClick?.invoke(user, REMOVE_USER)
        }
        holder.binding.imvEdit.click {
            onItemClick?.invoke(user, EDIT_USER)
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<UserModel>) {
        users.clear()
        users.addAll(list)
        notifyDataSetChanged()
    }

}
