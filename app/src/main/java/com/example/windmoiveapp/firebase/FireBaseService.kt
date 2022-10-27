package com.example.windmoiveapp.firebase

import com.example.windmoiveapp.model.UserModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

object FireBaseService {
    private const val USERS = "users"
    private val db = Firebase.firestore


    fun addInfoUser(user: UserModel) {
        db.collection(USERS).document(user.uid).set(user)
            .addOnSuccessListener {

            }.addOnFailureListener {
        }
    }

    fun updateInfoUser(
        user: UserModel,
        vararg fieldUser: Pair<String, Any>,
        onSuccess: ((UserModel) -> Unit)? = null
    ) {
        db.collection(USERS).document(user.uid).update(fieldUser.toMap()).addOnCompleteListener {
            onSuccess?.invoke(user)
        }
    }

    fun getInfoAllUser(): List<UserModel> {
        val listUser = arrayListOf<UserModel>()
        db.collection(USERS).get().addOnSuccessListener {documents ->
            for (user in documents) {
                listUser.add(user.toObject())
            }
        }
        return listUser
    }

    fun getInfoUser(uid: String): UserModel? {
        var user: UserModel? = null
        db.collection(USERS).document(uid).get().addOnSuccessListener { documentSnapshot ->
            user = documentSnapshot.toObject()
        }
        return user
    }

    fun deleteInfoUser(uid: String): Boolean {
        var result = false
        db.collection(USERS).document(uid).delete().addOnSuccessListener {
            result = true
        }.addOnFailureListener {
           result = false
        }
        return result
    }

    fun getInfoUserWithCondition(field: String, value: Any, onFailure:((String) -> Unit)? = null): List<UserModel> {
        val listUser = arrayListOf<UserModel>()
        db.collection(USERS).whereEqualTo(field, value).get().addOnSuccessListener { documents ->
            for (document in documents) {
                listUser.add(document.toObject())
            }
        }.addOnFailureListener {
            onFailure?.invoke(it.message.toString())
            listUser.addAll(emptyList())
        }
        return listUser
    }

}