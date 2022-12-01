package com.example.windmoiveapp.viewmodels

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.windmoiveapp.firebase.FireBaseService
import com.example.windmoiveapp.model.UserModel
import com.example.windmoiveapp.model.convertToUserModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    var userModelLiveData = MutableLiveData<UserModel?>()
    var listAllUser: MutableLiveData<List<UserModel>> = MutableLiveData()
    var postImageStorageLiveData: MutableLiveData<String?> = MutableLiveData()
    fun signInWithEmailPassword(
        email: String,
        passWord: String,
        onResult: ((FirebaseUser?) -> Unit)? = null
    ) {
        viewModelScope.launch {
            FireBaseService.signInWithEmailAndPassword(email, passWord,
                onSuccess = {
                    onResult?.invoke(it)
                }, onFailure = {
                    onResult?.invoke(null)
                })
        }
    }

    fun getUserInfo(uid: String) {
        viewModelScope.launch {
            FireBaseService.getInfoUser(uid) {
                userModelLiveData.postValue(it)
            }
        }
    }

    fun addUserInfo(userModel: UserModel, onResultCallback: (Boolean) -> Unit) {
        viewModelScope.launch {
            FireBaseService.addInfoUser(userModel) {
                onResultCallback.invoke(it)
            }
        }
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
                        addUserInfo(firebaseUser.convertToUserModel()) {
                            if (it) {
                                updateInfoUser(
                                    user = firebaseUser.convertToUserModel(),
                                    fieldUser = hashMapOf(UserModel::password.name to passWord)
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

    fun updateInfoUser(user: UserModel, fieldUser: HashMap<String, Any>) {
        viewModelScope.launch {
            FireBaseService.updateInfoUser(user.uid ?: "", fieldUser) {
                if (it) {
                    userModelLiveData.postValue(user)
                } else {
                    userModelLiveData.postValue(null)
                }
            }
        }
    }

    fun updateInfoUserOnServer(userModel: UserModel) {
        viewModelScope.launch {
        FireBaseService.updateInfoUserSever(userModel)
        }
    }


    fun getAllUser() {
        FireBaseService.getInfoAllUser {
            listAllUser.postValue(it)
        }
    }

    fun getUserByID(uid: String) {
        viewModelScope.launch {
            FireBaseService.getInfoUser(uid) {

            }
        }
    }

    fun postImageOnServerStorage(imageUri: Uri, fileName: String) {
        viewModelScope.launch {
            FireBaseService.postImageToServerStorage(imageUri, fileName) {
                postImageStorageLiveData.postValue(it)
            }
        }
    }


}