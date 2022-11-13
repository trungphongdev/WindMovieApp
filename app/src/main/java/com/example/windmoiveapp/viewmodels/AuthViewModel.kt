package com.example.windmoiveapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.windmoiveapp.firebase.FireBaseService
import com.example.windmoiveapp.model.UserModel
import com.example.windmoiveapp.model.convertToUserModel
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    var accessTokenFBLiveData: MutableLiveData<AccessToken?> = MutableLiveData()
    var googleSignInLiveData: MutableLiveData<GoogleSignInAccount?> = MutableLiveData()
    var userModelLiveData = MutableLiveData<UserModel?>()

    fun signInWithEmailPassword(
        email: String,
        passWord: String,
    ) {
        viewModelScope.launch {
            FireBaseService.signInWithEmailAndPassword(email, passWord, onSuccess = {
                userModelLiveData.postValue(it?.convertToUserModel())
            }, onFailure = {
                userModelLiveData.postValue(null)
            })
        }
    }

    fun setValueUserModel(userModel: UserModel) {
        userModelLiveData.postValue(userModel)
    }

    fun setValueGoogleSignInAccount(googleSignInAccount: GoogleSignInAccount) {
        googleSignInLiveData.postValue(googleSignInAccount)
    }

    fun signUpWithEmailPassword(
        email: String,
        passWord: String,
        onResult: ((Boolean) -> Unit)? = null
    ) {
        viewModelScope.launch {
            FireBaseService.signUpWithEmailAndPassword(email, passWord, onSuccess = {
                if (it != null /*&& it.isEmailVerified*/) {
                    userModelLiveData.postValue(it.convertToUserModel())
                    FireBaseService.addInfoUser(it.convertToUserModel())
                    onResult?.invoke(true)/*
                } else {
                    verifyEmail(it ?: return@signUpWithEmailAndPassword) { isVerify ->
                        if (isVerify) {
                            userModelLiveData.postValue(it.convertToUserModel())
                            FireBaseService.addInfoUser(it.convertToUserModel())
                            onResult?.invoke(true)
                        } else {
                            onResult?.invoke(false)
                            userModelLiveData.postValue(null)
                        }
                    }*/
                }
            }, onFailure = {
                onResult?.invoke(false)
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