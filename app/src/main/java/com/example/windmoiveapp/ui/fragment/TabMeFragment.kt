package com.example.windmoiveapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import com.example.windmoiveapp.R
import com.example.windmoiveapp.adapter.ProfileItemAdapter
import com.example.windmoiveapp.constant.AccountPermission
import com.example.windmoiveapp.constant.ProfileItemType
import com.example.windmoiveapp.databinding.FragmentProfileScreenBinding
import com.example.windmoiveapp.extension.loadCircleImage
import com.example.windmoiveapp.extension.navigateWithAnim
import com.example.windmoiveapp.model.ProfileItemModel
import com.example.windmoiveapp.model.UserModel
import com.example.windmoiveapp.util.IS_ACCOUNT
import com.example.windmoiveapp.util.PrefUtil
import com.example.windmoiveapp.viewmodels.AuthViewModel


class TabMeFragment : BaseFragment<FragmentProfileScreenBinding>() {
    private val authenViewModel: AuthViewModel by activityViewModels()
    private val mProfileAdapter by lazy { ProfileItemAdapter() }
    private val listProfile: ArrayList<ProfileItemModel> = arrayListOf()

    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentProfileScreenBinding {
        return FragmentProfileScreenBinding.inflate(inflater, container, false)
    }

    override fun onViewInitialized(
        view: View,
        savedInstanceState: Bundle?,
        isViewCreated: Boolean
    ) {
        setUpAccount()
        initViewProfile()
        initListener()
        initObserver()
    }

    private fun initListener() {
        binding.headerBar.apply {
            setEventBackListener {
                super.onBackFragment()
            }
        }
        mProfileAdapter.setCallback {
            when (it) {
                ProfileItemType.MY_PROFILE -> {
                    navigateToScreen(R.id.updateUserFragment)
                }
                ProfileItemType.NOTIFICATION -> {
                    navigateToScreen(R.id.notifyFragment)
                }
                ProfileItemType.MY_FAVOURITE_LIST -> {
                    navigateToScreen(R.id.myListFragment)
                }
                ProfileItemType.USER_MANAGEMENT -> {
                    navigateToScreen(R.id.accountManagementFragment)
                }
                ProfileItemType.MOVIE_MANAGEMENT -> {
                    navigateToScreen(R.id.movieManagementFragment)
                }
                ProfileItemType.FEEDBACK_MANAGEMENT -> {
                    val typeAccount = authenViewModel.userModelLiveData.value
                    when (typeAccount?.accountPermission) {
                        AccountPermission.ADMIN.type -> {
                            navigateToScreen(R.id.feedBackFragment)
                        }
                        AccountPermission.USER.type -> {
                            navigateToScreen(R.id.chatLogFragment)
                        }
                        else -> {

                        }
                    }
                }
                ProfileItemType.STATIC_REPORT -> {
                    navigateToScreen(R.id.statisticFragment)
                }
                ProfileItemType.SHARE_APP -> {

                }
                ProfileItemType.LOGOUT -> {
                    activity?.restartApp()
                }
                ProfileItemType.VIP -> {
                    navigateToScreen(R.id.purchaseFragment)
                }
            }
        }
    }

    private fun navigateToScreen(id: Int) {
        findNavController().navigateWithAnim(id)
    }

    private fun initObserver() {
        authenViewModel.userModelLiveData.observe(viewLifecycleOwner) {
            invalidAccountPermission(it)
            binding.imvUser.loadCircleImage(it?.photoUrl)
            binding.tvUserName.text = it?.name
        }
    }

    private fun initViewProfile() {
        listProfile.clear()
        listProfile.addAll(initListProfile())
        mProfileAdapter.setList(initListProfile())
        binding.rvContainerProfile.adapter = mProfileAdapter
        binding.rvContainerProfile.itemAnimator = DefaultItemAnimator()
    }

    private fun invalidAccountPermission(user: UserModel?) {
        user?.let {
            if (user.accountPermission == AccountPermission.USER.type) {
                val listProfileTypeUser = listProfile.filter {
                    it.type == ProfileItemType.USER_MANAGEMENT ||
                    it.type == ProfileItemType.MOVIE_MANAGEMENT ||
                    it.type == ProfileItemType.STATIC_REPORT
                }
                listProfile.removeAll(listProfileTypeUser.toSet())
            }
        }
    }

    private fun initListProfile(): ArrayList<ProfileItemModel> {
        val mListProfile = ArrayList<ProfileItemModel>()
        var i = 0
        mListProfile.add(
            ProfileItemModel(
                ++i,
                R.drawable.updateprofile,
                getString(R.string.myProfileLabel),
                type = ProfileItemType.MY_PROFILE
            )
        )
        mListProfile.add(
            ProfileItemModel(
                ++i,
                R.drawable.vip,
                getString(R.string.vipLabel),
                type = ProfileItemType.VIP
            )
        )
        mListProfile.add(
            ProfileItemModel(
                ++i,
                R.drawable.ic_baseline_notifications_active_24,
                getString(R.string.notificationLineCentreLabel),
                type = ProfileItemType.NOTIFICATION
            )
        )
        mListProfile.add(
            ProfileItemModel(
                ++i,
                R.drawable.lovemovie,
                getString(R.string.myMovieListLabel),
                type = ProfileItemType.MY_FAVOURITE_LIST
            )
        )
        mListProfile.add(
            ProfileItemModel(
                ++i,
                R.drawable.management_user,
                getString(R.string.userManagementBreakLine),
                type = ProfileItemType.USER_MANAGEMENT
            )
        )
        mListProfile.add(
            ProfileItemModel(
                ++i,
                R.drawable.management_movie,
                getString(R.string.movieManagementBreakLine),
                type = ProfileItemType.MOVIE_MANAGEMENT
            )
        )
        mListProfile.add(
            ProfileItemModel(
                ++i,
                R.drawable.feedback,
                getString(R.string.feedbackManagementBreakLine),
                type = ProfileItemType.FEEDBACK_MANAGEMENT
            )
        )

        mListProfile.add(
            ProfileItemModel(
                ++i,
                R.drawable.report,
                getString(R.string.statisticBreakLine),
                type = ProfileItemType.STATIC_REPORT
            )
        )
        mListProfile.add(
            ProfileItemModel(
                ++i,
                R.drawable.share,
                getString(R.string.shareAppLabel),
                type = ProfileItemType.SHARE_APP
            )
        )
        mListProfile.add(
            ProfileItemModel(
                ++i,
                R.drawable.logout,
                getString(R.string.logOutLabel),
                type = ProfileItemType.LOGOUT
            )
        )
        return mListProfile
    }


    private fun setUpAccount() {
        val accountNo = context?.let { PrefUtil(it).getValue(IS_ACCOUNT, false) } ?: false
        binding.rvContainerProfile.isEnabled = accountNo
        binding.cvSupport.isEnabled = accountNo
        binding.tvUserName.setText("Guest")
    }

}
