package com.example.windmoiveapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.windmoiveapp.constant.ProfileItemType
import com.example.windmoiveapp.databinding.ItemProfileBinding
import com.example.windmoiveapp.model.ProfileItemModel

class ProfileItemAdapter : RecyclerView.Adapter<ProfileItemAdapter.ProfileItemViewHolder>() {
    private var mListProfileItem = ArrayList<ProfileItemModel>()
    private var mCallback: ((ProfileItemType) -> Unit)? = null
    private var mCountAlertProfile = 0

    inner class ProfileItemViewHolder(val itemBinding: ItemProfileBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileItemViewHolder {
        return ProfileItemViewHolder(
            ItemProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    fun setCallback(callback: (ProfileItemType) -> Unit) {
        mCallback = callback
    }

    override fun getItemCount() = mListProfileItem.size

    override fun onBindViewHolder(holder: ProfileItemViewHolder, position: Int) {
        val dataItem = mListProfileItem[position]
        holder.itemBinding.apply {
      /*      if (dataItem.type == ProfileItemType.NOTIFICATION_CENTRE) {
                cvTotalNotification.isVisible = mCountAlertProfile != 0
                tvTotalNotificationUnread.text =  if (mCountAlertProfile > WatchListScreen.VALUE_MAX_COUNT_NOTIFY) root.context.getString(R.string.notifyCountMaxLabel) else mCountAlertProfile.toString()
            }*/
            ivIconProfile.setImageResource(dataItem.src)
            tvItemProfile.text = dataItem.name
            root.setOnClickListener {
                mCallback?.invoke(dataItem.type)
            }
        }
    }

    fun setList(list: ArrayList<ProfileItemModel>) {
        mListProfileItem = ArrayList(list)
        notifyDataSetChanged()
    }
}
