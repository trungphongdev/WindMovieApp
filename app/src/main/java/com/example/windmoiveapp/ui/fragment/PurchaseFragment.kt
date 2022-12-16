package com.example.windmoiveapp.ui.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.windmoiveapp.constant.AccountType
import com.example.windmoiveapp.databinding.LayoutPurchaseBinding
import com.example.windmoiveapp.extension.click
import com.example.windmoiveapp.extension.showAlertDialog
import com.example.windmoiveapp.model.PurchaseModel
import com.example.windmoiveapp.model.UserModel
import com.example.windmoiveapp.model.getUserWithUUID
import com.example.windmoiveapp.viewmodels.AuthViewModel
import com.example.windmoiveapp.viewmodels.MovieViewModel
import java.util.Calendar

class PurchaseFragment : BaseFragment<LayoutPurchaseBinding>() {
    private val authenViewModel: AuthViewModel by activityViewModels()
    private val viewModel: MovieViewModel by viewModels()
    private var userModel: UserModel? = null
    private var typePrice: Int = VIP1_PRICE

    companion object {
        const val VIP1_PRICE = 59000
        const val VIP2_PRICE = 399000
        const val ONE_DAY_MILLI = 86400000L
    }

    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): LayoutPurchaseBinding {
        return LayoutPurchaseBinding.inflate(inflater, container, false)
    }

    override fun onViewInitialized(
        view: View,
        savedInstanceState: Bundle?,
        isViewCreated: Boolean
    ) {
        initListener()
        initObserver()
    }

    private fun initObserver() {
        authenViewModel.userModelLiveData.observe(viewLifecycleOwner) {
            userModel = it
            if (it?.accountType == AccountType.VIP.type) {
                binding.tvPurchase.alpha = 0.6f
                binding.tvPurchase.isEnabled = false
                activity?.showAlertDialog("You upgraded your vip package!!!")
            }
        }
        viewModel.isUserPurchase.observe(viewLifecycleOwner) {
            if (it) {
                authenViewModel.updateInfoUser(userModel ?: return@observe,
                    hashMapOf(UserModel::accountType.name to AccountType.VIP.type))
                activity?.showAlertDialog("Buy VIP package Successfully!!!") {
                  //  authenViewModel.getUserInfo(userModel?.uid ?: "")
                    authenViewModel.userModelLiveData.postValue(userModel.apply {
                        this?.accountType = AccountType.VIP.type
                    })
                    onBackFragment()
                }

            } else {
                activity?.showAlertDialog("Buy VIP package Failure!!!")
            }
            dismissProgress()
        }
    }

    private fun initListener() {
        binding.tvPurchase.click {
            showProgress()
            val endDate = if (typePrice == VIP1_PRICE) {
                Calendar.getInstance().timeInMillis + (ONE_DAY_MILLI * Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH))
            } else {
                Calendar.getInstance().timeInMillis + (ONE_DAY_MILLI * Calendar.getInstance().get(Calendar.DAY_OF_YEAR))
            }
            val purchaseModel = PurchaseModel(userModel = userModel ?: return@click, price = typePrice,
            endDate = endDate)
            viewModel.purchaseVip(purchaseModel)
        }
        binding.headerBar.apply {
            setEventBackListener {
                onBackFragment()
            }
        }
    }
}