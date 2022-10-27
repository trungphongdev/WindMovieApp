package com.example.windmoiveapp.firebase

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.example.windmoiveapp.R
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import kotlinx.coroutines.launch

object FacebookService {
    fun LoginButton.loginWithAccountFb(
        onError: ((String) -> Unit)? = null
    ) {
            registerCallback(
                CallbackManager.Factory.create(),
                object : FacebookCallback<LoginResult> {
                    override fun onCancel() {
                        onError?.invoke(context.getString(R.string.errorMessageLabel))
                    }

                    override fun onError(error: FacebookException) {
                        onError?.invoke(error.message.toString())
                    }

                    override fun onSuccess(result: LoginResult) {

                    }
                })
        }
    }

    //private

    private fun isUserLoggedInFb(
        context: Context,
        onError: ((String) -> Unit)? = null
    ) {
            LoginManager.getInstance().retrieveLoginStatus(context, object : LoginStatusCallback {
                override fun onCompleted(accessToken: AccessToken) {
                }

                override fun onError(exception: Exception) {
                    onError?.invoke(exception.message.toString())
                }

                override fun onFailure() {
                    onError?.invoke(context.getString(R.string.errorMessageLabel))
                }
            })
        }
    }


    fun accessTokenTrackerFb() {
            object : AccessTokenTracker() {
                override fun onCurrentAccessTokenChanged(
                    oldAccessToken: AccessToken?,
                    currentAccessToken: AccessToken?
                ) {
                }
            }
        }

    fun signOutFb() {
            if (AccessToken.isCurrentAccessTokenActive()) {
                LoginManager.getInstance().logOut()
            }
        }
}