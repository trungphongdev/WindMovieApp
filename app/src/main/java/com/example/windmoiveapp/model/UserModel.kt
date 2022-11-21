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
    var photoUrl: String = DEFAULT_IMG_USER,
    var accountType: Int? = AccountType.USER.type,
    var accountPermission: Int? = AccountPermission.NORMAL.type,
    var password: String? = ""
) {
    companion object {
        const val PREF_USER = "PREF_USER"
        const val DEFAULT_IMG_USER = "https://lh3.googleusercontent.com/a/ALm5wu3p26QqdgIsoCMAU_wX3gsMy24bgtSM7ajkDLd9"
    }

}


fun FirebaseUser.convertToUserModel(): UserModel {
    return UserModel(
        uid = this.uid,
        name = this.displayName ?: getUserWithUUID(),
        email = this.email,
        phone = this.phoneNumber ?: "",
        photoUrl = this.photoUrl?.toString() ?: UserModel.DEFAULT_IMG_USER,
    )
}

fun GoogleSignInAccount.convertToUserModel(): UserModel {
    return UserModel(
        uid = this.id,
        name = this.displayName,
        email = this.email,
        photoUrl = this.photoUrl?.toString() ?: UserModel.DEFAULT_IMG_USER,
    )
}

fun getUserWithUUID(): String {
    return "User ${UUID.randomUUID()}"
}