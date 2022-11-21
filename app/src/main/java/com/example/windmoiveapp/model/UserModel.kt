package com.example.windmoiveapp.model

import com.example.windmoiveapp.constant.AccountPermission
import com.example.windmoiveapp.constant.AccountType
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseUser
import java.util.*

data class UserModel(
    var uid: String? = "",
    var name: String? = "",
    var email: String? = "",
    var phone: String? = "",
    var photoUrl: String? = "",
    var accountType: Int? = AccountType.USER.type,
    var accountPermission: Int? = AccountPermission.NORMAL.type,
    var password: String? = ""
) {
    companion object {
        const val PREF_USER = "PREF_USER"
    }

}


fun FirebaseUser.convertToUserModel(): UserModel {
    return UserModel(
        uid = this.uid,
        name = this.displayName ?: getUserWithUUID(),
        email = this.email,
        phone = this.phoneNumber ?: "",
        photoUrl = this.photoUrl?.toString() ?: "",
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

fun getUserWithUUID(): String {
    return "User ${UUID.randomUUID()}"
}