package com.example.windmoiveapp.model

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseUser

data class UserModel(
    val uid: String? = "",
    val name: String? = "",
    val email: String? = "",
    val phone: String? = "",
    val statusAccount: String? = "",
    val photoUrl: String? = "",
    val typeLogin: String? = "",
) {
    companion object {
        const val PREF_USER = "PREF_USER"
    }

}

fun FirebaseUser.convertToUserModel(): UserModel {
    return UserModel(
        uid = this.uid,
        name = this.displayName,
        email = this.email,
        phone = this.phoneNumber,
        photoUrl = this.photoUrl.toString()
    )
}

fun GoogleSignInAccount.convertToUserModel(): UserModel {
    return UserModel(
        uid = this.id,
        name = this.displayName,
        email = this.email,
        photoUrl = this.photoUrl.toString(),
    )
}