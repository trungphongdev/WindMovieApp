package com.example.windmoiveapp.ui.fragment

import android.app.Application
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.windmoiveapp.R
import com.example.windmoiveapp.databinding.FragmentSignUpBinding
import com.example.windmoiveapp.extension.*
import com.example.windmoiveapp.viewmodels.AuthViewModel

class SignUpFragment : BaseFragment<FragmentSignUpBinding>() {
    private val authenViewModel by lazy { AuthViewModel(activity?.application as Application) }

    companion object {
        @JvmStatic
        fun newInstance() = SignUpFragment()
    }

    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentSignUpBinding {
        return FragmentSignUpBinding.inflate(inflater, container, false)
    }

    override fun onViewInitialized(
        view: View,
        savedInstanceState: Bundle?,
        isViewCreated: Boolean
    ) {
        initObserver()
        initListener()
        initViews()
    }

    private fun initViews() {
        binding.edtPassword.apply {
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            setSwitchPasswordDialog()
        }
    }

    private fun initObserver() {
        authenViewModel.userModelLiveData.observe(viewLifecycleOwner) {

        }
    }

    private fun initListener() {
        binding.btnSignUp.click {
            if (invalidEmailPassword().not()) {
                activity?.showAlertDialog(getString(R.string.emailPasswordFailLabel))
            } else {
                showProgress()
                signUpWithEmailPassword()
            }
        }

        binding.edtPassword.afterTextChange {
            validateEnableButtonLogin()
        }

        binding.imgBack.setOnClickListener {
            onBackFragment()
        }

    }

    private fun invalidEmailPassword(): Boolean {
        val emailIsValid = binding.edtEmail.isValidEmail()
        val passwordIsValid = binding.edtPassword.isValidString()
        return emailIsValid && passwordIsValid
    }

    private fun signUpWithEmailPassword() {
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()
        authenViewModel.signUpWithEmailPassword(email, password, onResult = {
            dismissProgress()
            if (it) {
                navigateToHomeScreen()
            } else {
                activity?.showAlertDialog(getString(R.string.signUpFailLabel))
            }
        })
    }

    private fun navigateToHomeScreen() {
        findNavController().navigateWithAnim(R.id.dashBroadScreen)
    }

    private fun validateEnableButtonLogin() {
        binding.btnSignUp.apply {
            isEnabled = binding.edtEmail.isValidEmail() && binding.edtPassword.isValidString()
            alpha = if (isEnabled) 1.0f else 0.6f
        }
    }


}