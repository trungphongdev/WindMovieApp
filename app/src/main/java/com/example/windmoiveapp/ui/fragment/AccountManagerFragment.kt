package com.example.windmoiveapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.windmoiveapp.adapter.ManagerUsersAdapter
import com.example.windmoiveapp.databinding.FragmentManagerUsersBinding
import com.example.windmoiveapp.viewmodels.AuthViewModel


class AccountManagerFragment : BaseFragment<FragmentManagerUsersBinding>() {
    private val adapter: ManagerUsersAdapter by lazy { ManagerUsersAdapter() }
    private val viewModels: AuthViewModel by activityViewModels()


    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentManagerUsersBinding {
        return FragmentManagerUsersBinding.inflate(inflater, container, false)
    }

    override fun onViewInitialized(
        view: View,
        savedInstanceState: Bundle?,
        isViewCreated: Boolean
    ) {
        initViews()
        initObserver()

    }

    private fun initViews() {
        binding.apply {
            rcvUsers.adapter = adapter
            rcvUsers.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            rcvUsers.itemAnimator = DefaultItemAnimator()
        }
    }

    private fun initObserver() {
        viewModels.listAllUser.observe(viewLifecycleOwner) {
            dismissProgress()
            binding.llEmptyData.root.isGone = it.isNotEmpty()
            adapter.setList(it)
        }
    }

    override fun loadData() {
        super.loadData()
        showProgress()
        viewModels.getAllUser()
    }

}
