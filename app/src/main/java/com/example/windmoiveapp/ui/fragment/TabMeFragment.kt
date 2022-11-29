package com.example.windmoiveapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.windmoiveapp.R
import com.example.windmoiveapp.constant.AccountPermission
import com.example.windmoiveapp.databinding.FragmentInfoUserBinding
import com.example.windmoiveapp.extension.click
import com.example.windmoiveapp.extension.navigateWithAnim
import com.example.windmoiveapp.model.UserModel
import com.example.windmoiveapp.viewmodels.AuthViewModel


class TabMeFragment : BaseFragment<FragmentInfoUserBinding>() {
    private val authenViewModel: AuthViewModel by activityViewModels()

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
        initObserver()
    }

    private fun initListener() {
        binding.llUpdateUser.click {
            navigateToScreen(R.id.updateUserFragment)
        }
        binding.llMyList.click {
            navigateToScreen(R.id.myListFragment)
        }
        binding.llUsers.click {
            navigateToScreen(R.id.accountManagementFragment)
        }
        binding.llMovies.click {
            navigateToScreen(R.id.movieManagementFragment)
        }
        binding.llReport.click {
            navigateToScreen(R.id.statisticFragment)
        }
        binding.llFeedback.click {

        }
        binding.llLogout.click {

        }
        binding.headerBar.apply {
            setEventBackListener {
                super.onBackFragment()
            }
        }

    }

    private fun navigateToScreen(id: Int) {
        findNavController().navigateWithAnim(id)
    }

    private fun initObserver() {
        authenViewModel.userModelLiveData.observe(viewLifecycleOwner) {
            invalidAccountPermission(it)
        }
    }

    private fun invalidAccountPermission(user: UserModel?) {
        user?.let {
            if (user.accountPermission == AccountPermission.USER.type) {
                binding.llReport.isGone = true
                binding.llMovies.isGone = true
                binding.llUsers.isGone = true
            }
        }
    }

}
