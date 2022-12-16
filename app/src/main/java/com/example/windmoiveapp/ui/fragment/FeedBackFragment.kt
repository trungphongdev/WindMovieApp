package com.example.windmoiveapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.windmoiveapp.R
import com.example.windmoiveapp.adapter.MessageAdapter
import com.example.windmoiveapp.databinding.FragmentFeedBackBinding
import com.example.windmoiveapp.extension.loadCircleImage
import com.example.windmoiveapp.extension.navigateWithAnim
import com.example.windmoiveapp.viewmodels.AuthViewModel
import com.example.windmoiveapp.viewmodels.MovieViewModel

class FeedBackFragment : BaseFragment<FragmentFeedBackBinding>() {

    private val authenViewModel: AuthViewModel by activityViewModels()
    private val movieVM: MovieViewModel by viewModels()
    private val adapter by lazy { MessageAdapter() }

    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentFeedBackBinding {
        return FragmentFeedBackBinding.inflate(inflater, container, false)
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
        binding.headerBar.apply {
            setEventBackListener {
                onBackFragment()
            }
        }
        adapter.onItemClick = {
            findNavController().navigateWithAnim(
                R.id.chatLogFragment, bundleOf(
                    ChatLogFragment.IS_ADMIN to it
                )
            )
        }
    }

    private fun initObserver() {
        authenViewModel.userModelLiveData.observe(viewLifecycleOwner) {
            binding.imvUser.loadCircleImage(it?.photoUrl)
            binding.tvUsername.text = it?.name + "ADMIN"
        }
        authenViewModel.listAllUser.observe(viewLifecycleOwner) {
            dismissProgress()
            adapter.setList(it)
        }
//        movieVM.userFeedback.observe(viewLifecycleOwner) {
//            dismissProgress()
//           // setListUserFeedback()
//        }
    }

//    private fun setListUserFeedback() {
//        val users = authenViewModel.listAllUser.value
//        //val usersFeedback = movieVM.userFeedback.value?.distinctBy { it.fromId }
//        val list = arrayListOf<UserModel>()
//        if (users.isNullOrEmpty().not()) {
//            usersFeedback?.forEach { userFB ->
//                val user = users?.find { it.uid == userFB.fromId }
//                if (user != null) {
//                    list.add(user)
//                }
//            }
//        }
//    }

    private fun initViews() {
        binding.rvMessages.adapter = adapter
        binding.rvMessages.layoutManager =
            LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)
    }

    override fun loadData() {
        super.loadData()
        showProgress()
        authenViewModel.getAllUser()

    }
}