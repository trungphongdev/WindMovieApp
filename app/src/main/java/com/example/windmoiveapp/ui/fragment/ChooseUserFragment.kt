package com.example.windmoiveapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.windmoiveapp.R
import com.example.windmoiveapp.databinding.FragmentChooseTypeUserBinding
import com.example.windmoiveapp.databinding.LayoutPurchaseBinding
import com.example.windmoiveapp.extension.click
import com.example.windmoiveapp.extension.navigateWithAnim
import com.example.windmoiveapp.util.IS_ACCOUNT
import com.example.windmoiveapp.util.PrefUtil

class ChooseUserFragment : BaseFragment<FragmentChooseTypeUserBinding>() {
    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentChooseTypeUserBinding {
        return FragmentChooseTypeUserBinding.inflate(inflater, container, false)
    }

    override fun onViewInitialized(
        view: View,
        savedInstanceState: Bundle?,
        isViewCreated: Boolean
    ) {
        initListener()
    }

    private fun initListener() {
        binding.tvAdult.click {
            findNavController().navigateWithAnim(R.id.startPageFragment)
        }
        binding.tvKids.click {
            saveAccountNo()
            moveToDashBoard()
        }
    }

    private fun saveAccountNo() {
        context?.let {
            PrefUtil(it).putValue(IS_ACCOUNT, false)
        }
    }
}