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
    private var userModel: UserModel = UserModel()
    private val getImageResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            userUri = uri
            binding.imvUser.setImageURI(userUri)
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

    }

    private fun invalidUpdateUser() {
        binding.llChangeAvt.isEnabled = permissionUpdate
        binding.edtEmail.isEnabled = permissionUpdate
        binding.edtName.isEnabled = permissionUpdate
        binding.llGender.isEnabled = permissionUpdate
        binding.edtPhone.isEnabled = permissionUpdate
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
        binding.headerBar.apply {
            setEventBackListener {
                super.onBackFragment()
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
                userModel = UserModel(
                    name = binding.edtName.text.toString(),
                    email = binding.edtEmail.text.toString(),
                    phone = binding.edtPhone.text.toString(),
                    gender = typeGender
                )
                postImageOnStorageServer()
            } else {
                context?.showAlertDialog(getString(R.string.emailAndPasswordNotEmpty))
            }
        }
    }

    private fun postImageOnStorageServer() {
        val fileName = binding.edtId.text.toString()
        authenViewModel.postImageOnServerStorage(userUri ?: return, fileName)
    }

    private fun pickImageFromGallery() {
        getImageResult.launch("image/*")
    }

    private fun initObserver() {
        authenViewModel.postImageStorageLiveData.observe(viewLifecycleOwner) {
            if (it == null) {
                activity?.showAlertDialog(getString(R.string.postImageFail)) {
                    binding.imvUser.setImageResource(R.drawable.ic_baseline_account_circle_24)
                }
            } else {
                userModel.photoUrl = it
                authenViewModel.updateInfoUser(userModel, updateUserModel(userModel))
                authenViewModel.updateInfoUserOnServer(userModel)
            }
        }
        authenViewModel.userModelLiveData.observe(viewLifecycleOwner) {
           bindDataUserModelToViews(it ?: return@observe)
        }
    }


    private fun bindDataUserModelToViews(userModel: UserModel) {
        binding.apply {
            imvUser.loadImage(userModel.photoUrl)
            edtId.setText(userModel.uid)
            edtName.setText(userModel.name)
            edtEmail.setText(userModel.email.ifBlankOrNull())
            edtPhone.setText(userModel.phone.ifBlankOrNull())
            edtAccountType.setText(AccountType.getAccountByType(userModel).name)
            if (userModel.gender != GenderType.NOTHING.type) {
                rdGender.check(rdGender.getChildAt(GenderType.getGenderByType(userModel).type).id)
            }
        }
    }

}