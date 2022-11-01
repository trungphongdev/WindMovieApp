package com.example.windmoiveapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.windmoiveapp.R
import com.example.windmoiveapp.firebase.FireBaseService
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    var accessTokenFBLiveData: MutableLiveData<AccessToken?> = MutableLiveData()
    var accessTokenGoogleLiveData: MutableLiveData<GoogleSignInAccount?> = MutableLiveData()
    var userModelLiveData = MutableLiveData<FirebaseUser?>()

    fun signInWithEmailPassword(
        email: String,
        passWord: String,
        onError: ((String) -> Unit)? = null
    ) {
        viewModelScope.launch {
            FireBaseService.signInWithEmailAndPassword(email, passWord, onSuccess = {
                userModelLiveData.postValue(it)
            }, onFailure = {
                onError?.invoke(getApplication<Application>().getString(R.string.signInFailLabel))
            })
        }
    }

    fun signUpWithEmailPassword(email: String, passWord: String) {
        viewModelScope.launch {
            FireBaseService.signUpWithEmailAndPassword(email, passWord, onSuccess = {
                userModelLiveData.postValue(it)
            }, onFailure = {
                getApplication<Application>().getString(R.string.signUpFailLabel)
            })
        }
    }

    fun verifyEmail(firebaseUser: FirebaseUser, onResultCallback: (Boolean) -> Unit) {
        viewModelScope.launch {
            FireBaseService.verifyEmail(firebaseUser, onResult = {
                onResultCallback.invoke(it)
            })
        }
    }


}