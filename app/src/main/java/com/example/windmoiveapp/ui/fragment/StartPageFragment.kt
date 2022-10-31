package com.example.windmoiveapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.windmoiveapp.R
import com.example.windmoiveapp.adapter.IntroPagerAdapter
import com.example.windmoiveapp.databinding.FragmentStartPageBinding
import com.example.windmoiveapp.extension.click
import com.example.windmoiveapp.extension.navigateWithAnim

class StartPageFragment : BaseFragment<FragmentStartPageBinding>() {
    private var param1: String? = null
    private var param2: String? = null

    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            StartPageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentStartPageBinding {
        return FragmentStartPageBinding.inflate(inflater, container, false)
    }

    override fun onViewInitialized(
        view: View,
        savedInstanceState: Bundle?,
        isViewCreated: Boolean
    ) {
        initListener()
        initViewPager()
    }

    private fun initViewPager() {
        binding.vpContent.apply {
            adapter = IntroPagerAdapter(context)
        }
        binding.tabLayout.setupWithViewPager(binding.vpContent, true)
    }

    private fun initListener() {
        binding.btnLogin.click {
            navigateToLoginScreen()
        }
        binding.btnSignUp.click {
            navigateToSignUpScreen()
        }
    }

    private fun navigateToSignUpScreen() {
        activity?.let {
            findNavController().navigateWithAnim(R.id.signUpFragment)
        }
    }

    private fun navigateToLoginScreen() {
        activity?.let {
            findNavController().navigateWithAnim(R.id.loginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}