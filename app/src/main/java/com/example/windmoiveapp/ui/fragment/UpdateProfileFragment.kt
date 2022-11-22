package com.example.windmoiveapp.ui.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.example.windmoiveapp.R
import com.example.windmoiveapp.constant.AccountType
import com.example.windmoiveapp.constant.GenderType
import com.example.windmoiveapp.constant.TypeLogin
import com.example.windmoiveapp.databinding.FragmentUpdateInfoUserBinding
import com.example.windmoiveapp.extension.*
import com.example.windmoiveapp.model.UserModel
import com.example.windmoiveapp.model.updateUserModel
import com.example.windmoiveapp.viewmodels.AuthViewModel


class UpdateProfileFragment: BaseFragment<FragmentUpdateInfoUserBinding>() {
    private val authenViewModel: AuthViewModel by activityViewModels()
    private var permissionUpdate: Boolean = false
    private var userUri: Uri? = null
    private val getImageResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            userUri = uri
        }
    private var typeGender = GenderType.NOTHING.type


    companion object {
        const val BUNDLE_PERMISSION_UPDATE =  "BUNDLE_PERMISSION_UPDATE"
        private const val regexString =  "^([A-Za-z]+)(\\s[A-Za-z]+)*\\s?$"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionUpdate = arguments?.getBoolean(BUNDLE_PERMISSION_UPDATE) ?: false
    }

    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentUpdateInfoUserBinding {
        return FragmentUpdateInfoUserBinding.inflate(inflater)
    }

    override fun onViewInitialized(
        view: View,
        savedInstanceState: Bundle?,
        isViewCreated: Boolean
    ) {
        invalidUpdateUser()
        initListener()
        initObserver()
    }

    private fun initViews() {
        authenViewModel.userModelLiveData.observe(viewLifecycleOwner) {
            dismissProgress()
            if (it != null) {
                bindDataUserModelToViews(it)
            }
        }
    }

    private fun invalidUpdateUser() {
        val childCount = binding.root.childCount
        for (i in 0.until(childCount)) {
            binding.root.getChildAt(i).isEnabled = permissionUpdate
        }
        binding.tlUserId.isEnabled = false
        binding.tvOption.isVisible = permissionUpdate
    }

    private fun initListener() {
        binding.llChangeAvt.click {
            pickImageFromGallery()
        }
        binding.tvOption.click {
            updateUser()
        }
        binding.rdGender.setOnCheckedChangeListener { _, index ->
            typeGender = when (index) {
                GenderType.FEMALE.type -> {
                    GenderType.FEMALE.type
                }
                GenderType.MALE.type -> {
                    GenderType.MALE.type
                }
                else -> {
                    GenderType.NOTHING.type
                }
            }
        }
    }

    private fun updateUser() {
        val user = authenViewModel.userModelLiveData.value ?: return
        showProgress()
        binding.apply {
                when(user.typeLogin) {
                     TypeLogin.FIREBASE.name -> {
                         invalidUpdateUserViaFirebase(user)
                     }
                    TypeLogin.FACEBOOK.name -> {
                        invalidUpdateUserViaFB(user)
                    }
                    TypeLogin.GOOGLE.name -> {
                        invalidUpdateUserViaGG(user)
                    }

                }
            }
        }

    private fun invalidUpdateUserViaGG(user: UserModel) {

    }

    private fun invalidUpdateUserViaFB(user: UserModel) {

    }

    private fun invalidUpdateUserViaFirebase(user: UserModel) {
        binding.apply {
            if (edtName.isValidText() && edtEmail.isValidEmail()) {
                val data = UserModel(
                    name = edtName.text.toString(),
                    email = edtEmail.text.toString(),
                    phone = edtPhone.text.toString(),
                    gender = typeGender,
                    photoUrl = userUri?.toString() ?: user.photoUrl

                )
                authenViewModel.updateInfoUser(user, updateUserModel(data))
                authenViewModel.updateInfoUserOnServer(user)
            } else {
                context?.showAlertDialog(getString(R.string.emailAndPasswordNotEmpty))
            }
        }
    }

    private fun pickImageFromGallery() {
        getImageResult.launch("image/*")
    }

    private fun initObserver() {
        authenViewModel.userModelLiveData.observe(viewLifecycleOwner) {
           bindDataUserModelToViews(it ?: return@observe)
        }
    }


    private fun bindDataUserModelToViews(userModel: UserModel) {
        binding.apply {
            imvUser.loadImage(userModel.photoUrl)
            edtId.setText(userModel.uid)
            edtName.setText(userModel.name)
            edtEmail.setText(userModel.email ?: "")
            edtPhone.setText(userModel.phone ?: "")
            edtAccountType.setText(AccountType.getAccountByType(userModel).name)
            rdGender.check(rdGender.getChildAt(GenderType.getGenderByType(userModel).type).id)

        }
    }
}