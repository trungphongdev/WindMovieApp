package com.example.windmoiveapp.ui.fragment

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.windmoiveapp.R
import com.example.windmoiveapp.databinding.FragmentLoginBinding
import com.example.windmoiveapp.extension.*
import com.example.windmoiveapp.firebase.FacebookService
import com.example.windmoiveapp.firebase.FireBaseService
import com.example.windmoiveapp.firebase.GoogleService
import com.example.windmoiveapp.model.UserModel
import com.example.windmoiveapp.model.UserModel.Companion.PREF_USER
import com.example.windmoiveapp.model.convertToUserModel
import com.example.windmoiveapp.util.PrefUtil
import com.example.windmoiveapp.viewmodels.AuthViewModel
import com.example.windmoiveapp.viewmodels.MovieViewModel
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task


class LoginFragment : BaseFragment<FragmentLoginBinding>() {
    private val authenViewModel: AuthViewModel by activityViewModels()
    private val pref by lazy { PrefUtil.getInstance(activity?.application as Application) }
    private var typeLogin: Int? = null
    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultActivity ->
            if (resultActivity.resultCode == Activity.RESULT_OK && resultActivity.data != null) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(resultActivity.data)
                handleSignInResult(task)
            } else {
                activity?.getAlertDialog(getString(R.string.signInFailLabel))
            }
        }

    companion object {
        private const val LOGIN_FIREBASE = 1

    }

    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentLoginBinding {
        return FragmentLoginBinding.inflate(inflater, container, false)
    }

    override fun onViewInitialized(
        view: View,
        savedInstanceState: Bundle?,
        isViewCreated: Boolean
    ) {
        initViews()
        initListener()
        initObserver()
        validateEnableButtonLogin()
    }

    override fun loadData() {
        super.loadData()
        authenViewModel.getAllUser()
    }

    private fun initViews() {
        isSaveUserInfo()
        binding.edtPassword.apply {
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            setSwitchPasswordDialog()
        }
    }

    private fun initListener() {
        binding.btnLogin.setOnClickListener {
            invalidLoginFirebase()
        }
        binding.ggLoginBtn.setOnClickListener {
            invalidSignInGG()
        }

        FacebookService.loginWithFaceBook(binding.fbLoginBtn) { user ->
            invalidSignInFb(user)
        }

        binding.edtPassword.afterTextChange {
            validateEnableButtonLogin()
        }

        binding.imgBack.setOnClickListener {
            onBackFragment()
        }
    }

    private fun initObserver() {
        authenViewModel.listAllUser.observe(viewLifecycleOwner) {}

        authenViewModel.userModelLiveData.observe(viewLifecycleOwner) { user ->
            dismissProgress()
            if (user != null) {
                navigateToHomeFragment()
                setUpInfoUserSignIn(user)
            } else {
                context?.showAlertDialog(getString(R.string.signInFailLabel))
            }
        }
    }

    private fun isSaveUserInfo() {
        context?.let {
            val userInString = PrefUtil.getInstance(it).getValue(PREF_USER, "")
            if (userInString.isBlank().not()) {
                val userModel = GsonExt.convertGsonToObjet(userInString, UserModel::class.java)
                binding.edtEmail.setText(userModel.email)
                binding.cbAccount.isChecked = true
            }
        }
    }

    private fun invalidLoginFirebase() {
        typeLogin = LOGIN_FIREBASE
        if (invalidEmailPassword()) {
            showProgress()
            setEventLogin()
        } else {
            context?.getAlertDialog(getString(R.string.emailPasswordFailLabel))
            dismissProgress()
        }
    }

    private fun invalidSignInFb(user: UserModel?) {
        if (user == null) {
            activity?.showAlertDialog(context?.getString(R.string.errorMessageLabel) ?: "")
        } else {
            val users = authenViewModel.listAllUser.value?.firstOrNull { user.uid == it.uid }
            if (users == null) {
                authenViewModel.addUserInfo(user) {
                }
            }
            authenViewModel.getUserInfo(user.uid ?: "")
        }
    }

    private fun validateEnableButtonLogin() {
        binding.btnLogin.apply {
            isEnabled = binding.edtEmail.isValidEmail() && binding.edtPassword.isValidString()
            alpha = if (isEnabled) 1.0f else 0.6f
        }
    }

    private fun setEventLogin() {
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()
        authenViewModel.signInWithEmailPassword(email, password) {
            authenViewModel.getUserInfo(it?.uid ?: "")
        }
    }

    private fun invalidEmailPassword(): Boolean {
        val emailIsValid = binding.edtEmail.isValidEmail()
        val passwordIsValid = binding.edtPassword.isValidString()
        return emailIsValid && passwordIsValid
    }

    private fun setUpInfoUserSignIn(user: UserModel) {
        when (typeLogin) {
            LOGIN_FIREBASE -> {
                if (binding.cbAccount.isChecked) {
                    pref.putValue(PREF_USER, GsonExt.convertObjetToGson(user))
                } else {
                    pref.removeKey(PREF_USER)
                }
            } else -> {}
        }
        moveToDashBoard()
    }

    private fun invalidSignInGG() {
        GoogleService.signUpBeforeLogin(activity ?: return)
        val signInIntent = GoogleService.loginWithAccountGg(activity ?: return).signInIntent
        startForResult.launch(signInIntent)
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            showProgress()
            val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
            val user = authenViewModel.listAllUser.value?.firstOrNull { it.uid == account.id }
            if (user == null) {
                authenViewModel.addUserInfo(account.convertToUserModel()) {
                }
            }
            authenViewModel.getUserInfo(account.convertToUserModel().uid ?: "")
        } catch (e: ApiException) {
            activity?.getAlertDialog(e.message ?: "")
        }
    }

    private fun navigateToHomeFragment() {
        findNavController().navigateWithAnim(R.id.dashBroadScreen)
    }


}
