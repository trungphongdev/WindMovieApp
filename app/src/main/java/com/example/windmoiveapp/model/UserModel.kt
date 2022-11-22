package com.example.windmoiveapp.model

import com.example.windmoiveapp.constant.AccountPermission
import com.example.windmoiveapp.constant.AccountType
import com.example.windmoiveapp.constant.GenderType
import com.example.windmoiveapp.constant.TypeLogin
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseUser
import java.util.*
import kotlin.collections.HashMap

data class UserModel(
    var uid: String? = "",
    var name: String? = "",
    var email: String? = "",
    var phone: String? = "",
    var gender: Int = GenderType.NOTHING.type,
    var photoUrl: String = DEFAULT_IMG_USER,
    var accountType: Int = AccountType.NORMAL.type,
    var accountPermission: Int = AccountPermission.USER.type,
    var password: String? = "",
    var typeLogin: String = ""
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
        typeLogin = TypeLogin.FIREBASE.name,
    )
}

fun GoogleSignInAccount.convertToUserModel(): UserModel {
    return UserModel(
        uid = this.id,
        name = this.displayName,
        email = this.email,
        photoUrl = this.photoUrl?.toString() ?: UserModel.DEFAULT_IMG_USER,
        typeLogin = TypeLogin.GOOGLE.name
    )
}

fun updateUserModel(userModel: UserModel): HashMap<String, Any> {
    return hashMapOf(
        userModel::name.name to (userModel.name ?: ""),
        userModel::email.name to (userModel.email ?: ""),
        userModel::phone.name to (userModel.phone ?: ""),
        userModel::gender.name to (userModel.gender),
        userModel::photoUrl.name to (userModel.photoUrl),
    )
}

fun getUserWithUUID(): String {
    return "User ${UUID.randomUUID()}"
}