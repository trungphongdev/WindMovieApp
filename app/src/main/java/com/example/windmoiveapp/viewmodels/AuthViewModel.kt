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
        onResult: ((UserModel?) -> Unit)? = null
    ) {
        viewModelScope.launch {
            FireBaseService.signUpWithEmailAndPassword(
                email,
                passWord,
                onSuccess = { firebaseUser ->
                    if (firebaseUser != null /*&& it.isEmailVerified*/) {
                        FireBaseService.addInfoUser(firebaseUser.convertToUserModel()) {
                            if (it) {
                                updateInfoUser(
                                    user = firebaseUser.convertToUserModel(),
                                    fieldUser = arrayOf(Pair("password", passWord))
                                )
                                onResult?.invoke(firebaseUser.convertToUserModel())
                            } else {
                                onResult?.invoke(null)
                            }
                        }
                    } else {
                        onResult?.invoke(null)
                    }
                },
                onFailure = {
                    onResult?.invoke(null)
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

    fun updateInfoUser(user: UserModel, vararg fieldUser: Pair<String, Any>) {
        FireBaseService.updateInfoUser(user.uid ?: "", *fieldUser) {
            if (it) {
                userModelLiveData.postValue(user)
            } else {
                userModelLiveData.postValue(null)
            }
        }
    }


}