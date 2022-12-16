package com.example.windmoiveapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.windmoiveapp.adapter.ChatLogAdapter
import com.example.windmoiveapp.constant.AccountPermission
import com.example.windmoiveapp.databinding.FragmentChatLogBinding
import com.example.windmoiveapp.extension.click
import com.example.windmoiveapp.model.ChatModel
import com.example.windmoiveapp.model.UserModel
import com.example.windmoiveapp.viewmodels.AuthViewModel
import com.example.windmoiveapp.viewmodels.MovieViewModel

class ChatLogFragment : BaseFragment<FragmentChatLogBinding>() {

    private val authenViewModel: AuthViewModel by activityViewModels()
    private val movieVM: MovieViewModel by viewModels()
    private val adapter by lazy { ChatLogAdapter() }
    private var chatModel = ChatModel()
    private var accAdmin: UserModel? = UserModel()
    private var userModel: UserModel? = null

    companion object {
        const val IS_ADMIN = "IS_ADMIN"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userModel = arguments?.getParcelable(IS_ADMIN) ?: return
    }

    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentChatLogBinding {
        return FragmentChatLogBinding.inflate(inflater, container, false)
    }

    override fun onViewInitialized(
        view: View,
        savedInstanceState: Bundle?,
        isViewCreated: Boolean
    ) {
        initViews()
        initObserver()
        initListener()
    }

    private fun initListener() {
        binding.btnSend.click {
            chatModel.text = binding.chatEdittext.text.toString()
            movieVM.sendFeedback(chatModel)
            binding.chatEdittext.text?.clear()
        }
        binding.headerBar.apply {
            setEventBackListener {
                onBackFragment()
            }
        }
    }

    private fun initObserver() {
        authenViewModel.listAllUser.observe(viewLifecycleOwner) {
            dismissProgress()
            accAdmin = it.find { user -> user.accountPermission == AccountPermission.ADMIN.type }
            setDataChatModel()
        }

        authenViewModel.userModelLiveData.observe(viewLifecycleOwner) {
            setDataChatModel()

        }
        movieVM.isSendFeedback.observe(viewLifecycleOwner) {
            binding.rcvChatLog.scrollToPosition(adapter.itemCount - 1)
        }
        movieVM.listChat.observe(viewLifecycleOwner) {
            val userSend = authenViewModel.userModelLiveData.value ?: return@observe
            adapter.setList(it, userSend, accAdmin ?: return@observe)
        }
    }

    private fun setDataChatModel() {
        if (userModel != null) {
            chatModel.apply {
                this.fromId = authenViewModel.userModelLiveData.value?.uid ?: ""
                this.toId = userModel?.uid ?: ""
            }
        } else {
            chatModel.apply {
                this.fromId = authenViewModel.userModelLiveData.value?.uid ?: ""
                this.toId = accAdmin?.uid ?: ""
            }
        }
        movieVM.getFeedback(chatModel)

    }


    private fun initViews() {
        binding.rcvChatLog.adapter = adapter
        binding.rcvChatLog.layoutManager =
            LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)
    }

    override fun loadData() {
        super.loadData()
        showProgress()
        if (userModel == null) {
            authenViewModel.getAllUser()
        }
    }
}