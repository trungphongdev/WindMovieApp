package com.example.windmoiveapp.firebase

import com.example.windmoiveapp.model.UserModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import timber.log.Timber

object FireBaseService {
    private const val USERS = "users"
    private val db = Firebase.firestore
    private val auth = Firebase.auth
    private const val TAG = "Firebase"


    fun addInfoUser(user: UserModel) {
        db.collection(USERS).document(user.uid ?: "").set(user)
            .addOnSuccessListener {

            }.addOnFailureListener {
            }
    }

    fun updateInfoUser(
        user: UserModel,
        vararg fieldUser: Pair<String, Any>,
        onSuccess: ((UserModel) -> Unit)? = null
    ) {
        db.collection(USERS).document(user.uid ?: "").update(fieldUser.toMap())
            .addOnCompleteListener {
                onSuccess?.invoke(user)
            }
    }

    fun getInfoAllUser(): List<UserModel> {
        val listUser = arrayListOf<UserModel>()
        db.collection(USERS).get().addOnSuccessListener { documents ->
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

    fun getInfoUserWithCondition(
        field: String,
        value: Any,
        onFailure: ((String) -> Unit)? = null
    ): List<UserModel> {
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

   suspend fun signUpWithEmailAndPassword(
       email: String,
       password: String,
       onSuccess: ((FirebaseUser?) -> Unit)? = null,
       onFailure: (() -> Unit)? = null
   ) {
       auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
           if (task.isSuccessful) {
               Timber.tag(TAG).d("createUserWithEmail:success")
               onSuccess?.invoke(auth.currentUser)
           } else {
                // If sign in fails, display a message to the user.
                Timber.tag(TAG).d("createUserWithEmail :failure" + task.exception)
                onFailure?.invoke()
            }
        }.addOnFailureListener {
            // If sign in fails, display a message to the user.
            Timber.tag(TAG).d("createUserWithEmail :failure" + it.message)
            onFailure?.invoke()
        }
    }

    fun signInWithEmailAndPassword(
        email: String,
        password: String,
        onSuccess: ((FirebaseUser?) -> Unit)? = null,
        onFailure: (() -> Unit)? = null
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Timber.tag(TAG).d("signInWithEmail:success")
                    onSuccess?.invoke(auth.currentUser)
                    //updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Timber.tag(TAG).w(task.exception, "signInWithEmail:failure")
                    //updateUI(null)
                    onFailure?.invoke()
                }
            }.addOnFailureListener {
                Timber.tag(TAG).w(it.message, "signInWithEmail:failure")
                onFailure?.invoke()
            }
    }

    fun verifyEmail(
        fireBaseUser: FirebaseUser,
        onResult: ((Boolean) -> Unit)? = null,
    ) {
        fireBaseUser.sendEmailVerification().addOnCompleteListener {
            if (it.isSuccessful) {
                onResult?.invoke(true)
            } else {
                onResult?.invoke(false)
            }
        }.addOnFailureListener {
            onResult?.invoke(false)
        }
    }

}
