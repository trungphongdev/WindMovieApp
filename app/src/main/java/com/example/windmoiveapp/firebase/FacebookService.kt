package com.example.windmoiveapp.firebase

import android.content.Context
import com.example.windmoiveapp.R
import com.example.windmoiveapp.constant.TypeLogin
import com.example.windmoiveapp.model.UserModel
import com.facebook.*
import com.facebook.internal.ImageRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton

object FacebookService {
    fun loginWithFaceBook(loginButton: LoginButton, onResult: ((UserModel?) -> Unit)? = null) {
        loginButton.loginWithAccountFb(onResult)
    }
    fun LoginButton.loginWithAccountFb(
        onResult: ((UserModel?) -> Unit)? = null
    ) {
        registerCallback(
            CallbackManager.Factory.create(),
            object : FacebookCallback<LoginResult> {
                override fun onCancel() {
                    onResult?.invoke(null)
                }

                override fun onError(error: FacebookException) {
                    onResult?.invoke(null)
                }

                override fun onSuccess(result: LoginResult) {
                    GraphRequest.newMeRequest(
                        result.accessToken
                    ) { obj, _ ->
                        try {
                            val userModel = UserModel(
                                uid = obj?.getString("id") ?: "",
                                name = obj?.getString("name"),
                                email = obj?.getString("email"),
                                photoUrl = ImageRequest.getProfilePictureUri(
                                    Profile.getCurrentProfile()?.id,
                                    200,
                                    200
                                ).toString(),
                                typeLogin = TypeLogin.FACEBOOK.name
                            )
                            onResult?.invoke(userModel)
                        } catch (e: Exception) {
                            onResult?.invoke(null)
                        }
                    }

                }
            })
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