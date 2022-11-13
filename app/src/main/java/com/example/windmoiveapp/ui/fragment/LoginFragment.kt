package com.example.windmoiveapp.ui.fragment

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.example.windmoiveapp.R
import com.example.windmoiveapp.databinding.FragmentLoginBinding
import com.example.windmoiveapp.extension.*
import com.example.windmoiveapp.firebase.FireBaseService
import com.example.windmoiveapp.firebase.GoogleService
import com.example.windmoiveapp.model.UserModel
import com.example.windmoiveapp.model.UserModel.Companion.PREF_USER
import com.example.windmoiveapp.model.convertToUserModel
import com.example.windmoiveapp.util.PrefUtil
import com.example.windmoiveapp.viewmodels.AuthViewModel
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
    private val authenViewModel by lazy { AuthViewModel(activity?.application as Application) }
    private val pref by lazy { PrefUtil.getInstance(activity?.application as Application) }
    private val listUser by lazy { FireBaseService.getInfoAllUser() }

    companion object {
        const val RC_SIGN_IN_CODE_SUCCESS = 1
        const val RC_SIGN_IN = 1
    }

    private fun navigateToHomeFragment() {
        findNavController().navigateWithAnim(R.id.dashBroadScreen)
    }

    override fun onDestroyView() {
        super.onDestroyView()

    }

    fun getUserInfo() {
        LoginManager.getInstance().logInWithReadPermissions(
            this,
            listOf("email")
        )
    }

    private fun signInWithGoogle() {
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        val googleSignInClient = GoogleSignIn.getClient(context ?: return, gso)
        val signInIntent = googleSignInClient.signInIntent
        activity?.startActivityForResult(signInIntent, RC_SIGN_IN_CODE_SUCCESS)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN_CODE_SUCCESS) {
            try {
                val googleSignInAccount = GoogleSignIn.getSignedInAccountFromIntent(data)
                    .getResult(ApiException::class.java)
                // authenViewModel.loginWithAccountGg(googleSignInAccount)
            } catch (e: ApiException) {
                // authenViewModel.loginWithAccountGg(null)
            }
        }
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

    private fun initViews() {
        isSaveUserInfo()
    }

    private fun isSaveUserInfo() {
        context?.let {
            val userInString = PrefUtil.getInstance(it).getValue(PREF_USER, "")
            if (userInString.isBlank().not()) {
                val userModel = GsonExt.convertGsonToObjet(userInString, UserModel::class.java)
                binding.edtEmail.setText(userModel.email)
            }
        }
    }

    private fun initListener() {
        binding.btnLogin.setOnClickListener {
            if (invalidEmailPassword()) {
                showProgress()
                setEventLogin()
            } else {
                context?.getAlertDialog(getString(R.string.emailPasswordFailLabel))
                dismissProgress()
            }
        }
        binding.ggLoginBtn.setOnClickListener {
            invalidSignInGG()
        }
        binding.fbLoginBtn.registerCallback(
            CallbackManager.Factory.create(),
            object : FacebookCallback<LoginResult> {
                override fun onCancel() {
                    activity?.showAlertDialog(context?.getString(R.string.errorMessageLabel) ?: "")
                }

                override fun onError(error: FacebookException) {
                    activity?.showAlertDialog(error.message.toString())
                }

                override fun onSuccess(result: LoginResult) {
                    //handleAccessToken(result.accessToken)
                    navigateToHomeFragment()
                }
            })

        binding.edtPassword.afterTextChange {
            validateEnableButtonLogin()
        }
    }

    private fun validateEnableButtonLogin() {
        binding.btnLogin.apply {
            isEnabled = binding.edtEmail.text.isNotBlank() && binding.edtPassword.text.isNotBlank()
            alpha = if (isEnabled) 1.0f else 0.6f
        }
    }

    private fun setEventLogin() {
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()
        authenViewModel.signInWithEmailPassword(email, password)
    }

    private fun invalidEmailPassword(): Boolean {
        val emailIsValid = binding.edtEmail.isValidEmail()
        val passwordIsValid = binding.edtPassword.isValidString()
        return emailIsValid && passwordIsValid
    }

    private fun initObserver() {
        authenViewModel.googleSignInLiveData.observe(viewLifecycleOwner) { ggSignIn ->
            val user = listUser.firstOrNull { it.uid == ggSignIn?.id }
            if (user == null) {
                FireBaseService.addInfoUser(ggSignIn?.convertToUserModel() ?: return@observe)
            }
            navigateToHomeFragment()
        }

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

    private fun setUpInfoUserSignIn(user: UserModel) {
        if (binding.cbAccount.isChecked) {
            pref.putValue(PREF_USER, GsonExt.convertObjetToGson(user))
        } else {
            pref.removeKey(PREF_USER)
        }
        findNavController().navigateWithAnim(R.id.homeFragment)
    }

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultActivity ->
            if (resultActivity.resultCode == Activity.RESULT_OK && resultActivity.data != null) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(resultActivity.data)
                handleSignInResult(task)
            } else {
                activity?.getAlertDialog(getString(R.string.signInFailLabel))
            }
        }

    private fun invalidSignInGG() {
//        val googleSignInAccount = GoogleService.isLoggedInGg(context ?: requireContext())
//        if (googleSignInAccount != null) {
//            googleSignInAccount.convertToUserModel()
//            navigateToHomeFragment()
//        } else {
        val signInIntent = GoogleService.loginWithAccountGg(activity ?: return).signInIntent
        startForResult.launch(signInIntent)
        //}
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
            authenViewModel.setValueGoogleSignInAccount(account)
        } catch (e: ApiException) {
            activity?.getAlertDialog(e.message ?: "")
        }
    }


}
