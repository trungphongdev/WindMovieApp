package com.example.windmoiveapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.windmoiveapp.R
import com.example.windmoiveapp.databinding.FragmentInfoUserBinding
import com.example.windmoiveapp.extension.navigateWithAnim

class AccountInfoFragment : BaseFragment<FragmentInfoUserBinding>() {

    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentInfoUserBinding {
        return FragmentInfoUserBinding.inflate(inflater, container, false)
    }

    override fun onViewInitialized(
        view: View,
        savedInstanceState: Bundle?,
        isViewCreated: Boolean
    ) {
        initListener()
    }

    private fun initListener() {
        binding.headerBar.apply {
            setEventBackListener {
                moveToDashBoard()
            }
        }
    }

    private fun navigateToLoginScreen() {
        activity?.let {
            findNavController().navigateWithAnim(R.id.loginFragment)
        }
    }


}