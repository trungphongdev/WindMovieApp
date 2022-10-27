package com.example.windmoiveapp.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.windmoiveapp.R
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    var accessTokenFBLiveData: MutableLiveData<AccessToken?> = MutableLiveData()
    var accessTokenGoogleLiveData: MutableLiveData<GoogleSignInAccount?> = MutableLiveData()



}