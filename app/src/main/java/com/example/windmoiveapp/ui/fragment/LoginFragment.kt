package com.example.windmoiveapp.ui.fragment

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.windmoiveapp.R
import com.example.windmoiveapp.databinding.FragmentLoginBinding
import com.example.windmoiveapp.extension.getAlertDialog
import com.example.windmoiveapp.extension.isValidEmail
import com.example.windmoiveapp.viewmodels.AuthViewModel
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import timber.log.Timber

class LoginFragment : BaseFragment<FragmentLoginBinding>() {
    private val authenViewModel by lazy { AuthViewModel(activity?.application as Application) }

    companion object {
        const val RC_SIGN_IN_CODE_SUCCESS = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun navigateToHomeFragment() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun signInWithFbAcc() {
        // authenViewModel.loginWithAccountGg()
    }

    private fun signedInWithGoogleAcc() {
        //authenViewModel.isLoggedInGg(context ?: return)
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
    }

    private fun initViews() {

    }

    private fun initListener() {
        binding.btnLogin.setOnClickListener {
            if (invalidEmailPassword()) {
                setEventLogin()
            } else {
                activity?.let {
                    it.getAlertDialog(getString(R.string.emailPasswordFailLabel))
                }
            }
        }
    }

    private fun setEventLogin() {
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()
        authenViewModel.signUpWithEmailPassword(email, password)
    }

    private fun invalidEmailPassword(): Boolean {
        val emailIsValid = binding.edtEmail.isValidEmail()
        val passwordIsValid = binding.edtPassword.text.isNullOrBlank().not()
        return emailIsValid && passwordIsValid
    }

    private fun initObserver() {
        authenViewModel.accessTokenGoogleLiveData.observe(viewLifecycleOwner) {
            if (it != null) {
                navigateToHomeFragment()
            } else {
                signInWithGoogle()
            }
        }
        authenViewModel.userModelLiveData.observe(viewLifecycleOwner) { fbUser ->
            if (fbUser != null && fbUser.isEmailVerified) {
                Log.d("oke", "oke")
                Timber.d(" Verify")
            } else {
                Log.d("oke333", "oke")

                Timber.d("Not Verify")
            }
        }
    }
}
