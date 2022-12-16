package com.example.windmoiveapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.windmoiveapp.databinding.ItemChatLeftBinding
import com.example.windmoiveapp.databinding.ItemChatRightBinding
import com.example.windmoiveapp.extension.loadCircleImage
import com.example.windmoiveapp.model.ChatModel
import com.example.windmoiveapp.model.UserModel
import java.text.SimpleDateFormat
import java.util.*

class ChatLogAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var chats: List<ChatModel> = listOf()
    var userSend = UserModel()
    var userRecieve = UserModel()

    companion object {
        private const val ITEM_RECIEVE = 1
        private const val ITEM_SENT = 2
    }

    var myProfile: String? = ""
    val formatter = SimpleDateFormat("dd/MM/yy hh:mm:ss", Locale.getDefault())
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 1) {
            val binding =
                ItemChatLeftBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ViewHolderChatLeft(binding)

        } else {
            val binding =
                ItemChatRightBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ViewHolderChatRight(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = chats[position]
        if (holder.javaClass == ViewHolderChatRight::class.java) {
            val viewHolder = holder as ViewHolderChatRight
            viewHolder.bindChatRight(currentMessage)

        }
        if (holder.javaClass == ViewHolderChatLeft::class.java) {
            val viewHolder = holder as ViewHolderChatLeft
            viewHolder.bindChatLeft(currentMessage)
        }

    }

    override fun getItemCount(): Int {
        return chats.size
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = chats[position]
        return if (userSend.uid.equals(currentMessage.fromId)) {
            ITEM_SENT
        } else {
            ITEM_RECIEVE
        }
    }

    inner class ViewHolderChatLeft(var binding: ItemChatLeftBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindChatLeft(chat: ChatModel) {
            binding.tvChatLeft.text = chat.text
            binding.tvTimeChatLeft.text = formatter.format(Date(chat.timestamp))
            binding.imvUserLeft.loadCircleImage(userRecieve.photoUrl)
        }
    }

    inner class ViewHolderChatRight(var binding: ItemChatRightBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindChatRight(chat: ChatModel) {
            binding.tvChatRight.text = chat.text
            binding.tvTimeRight.text = formatter.format(Date(chat.timestamp))
            binding.imvUserRight.loadCircleImage(userSend.photoUrl)
        }
    }

    fun setList(chats: List<ChatModel>, userSend: UserModel, userRecieve: UserModel) {
        this.chats = chats
        this.userSend = userSend
        this.userRecieve = userRecieve
        notifyDataSetChanged()
    }

}