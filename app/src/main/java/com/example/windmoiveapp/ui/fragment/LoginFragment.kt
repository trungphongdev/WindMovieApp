package com.example.windmoiveapp.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.windmoiveapp.databinding.FragmentLoginBinding
import com.example.windmoiveapp.viewmodels.AuthViewModel
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

class LoginFragment : Fragment() {
    private var binding: FragmentLoginBinding? = null
    private val authenViewModel by lazy { AuthViewModel() }

    private var param1: String? = null
    private var param2: String? = null

    companion object {
        const val RC_SIGN_IN_CODE_SUCCESS = 1
        private const val ARG_PARAM1 = ""
        private const val ARG_PARAM2 = ""

        @JvmStatic
        fun newInstance() =
            LoginFragment().apply {
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //binding?.fbLoginBtn?
        initObserver()
        initListener()
        signedInWithGoogleAcc()
    }

    private fun initListener() {
        TODO("Not yet implemented")
    }

    private fun initObserver() {
        authenViewModel.accessTokenGoogleLiveData.observe(viewLifecycleOwner) {
            if (it != null) {
                navigateToHomeFragment()
            } else {
                signInWithGoogle()
            }
        }
    }

    private fun navigateToHomeFragment() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
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
}
