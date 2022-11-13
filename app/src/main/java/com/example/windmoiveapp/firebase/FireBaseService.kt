package com.example.windmoiveapp.firebase

import android.util.Log
import com.example.windmoiveapp.constant.Categories
import com.example.windmoiveapp.model.MovieModel
import com.example.windmoiveapp.model.UserModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import timber.log.Timber

object FireBaseService {
    private const val USERS = "users"
    private const val MOVIES = "movies"
    private const val CATEGORIES = "categories"
    private const val DISLIKE_NUM = "dislikeNum"
    private const val LIKE_NUM = "likeNum"
    val db = Firebase.firestore
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
       if (auth.currentUser != null) {
           auth.signOut()
       }
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
                } else {
                    Timber.tag(TAG).w(task.exception, "signInWithEmail:failure")
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

    suspend fun getMovieList(list: ((List<MovieModel>) -> Unit)) {
        db.collection(MOVIES).get().addOnSuccessListener { result ->
            val listMovie = ArrayList<MovieModel>()
            for (movie in result.toObjects(MovieModel::class.java)) {
                listMovie.add(movie)
                Log.d("movies", "size" + listMovie.size)
            }
            list.invoke(listMovie)
        }.addOnFailureListener {
            list.invoke(emptyList())
        }
    }

    suspend fun likePostMovie(
        isLike: Boolean = false,
        movieModel: MovieModel,
        onResult: ((Boolean) -> Unit)?
    ) {
        val movie = db.collection(MOVIES).document(movieModel.id)
        if (isLike) {
            movie.update(LIKE_NUM, ((movieModel.likeNum ?: 0) + 1))
                .addOnCompleteListener { onResult?.invoke(true) }
                .addOnFailureListener { onResult?.invoke(false) }
        } else {
            movie.update(DISLIKE_NUM, ((movieModel.dislikeNum ?: 0) + 1))
                .addOnCompleteListener { onResult?.invoke(true) }
                .addOnFailureListener { onResult?.invoke(false) }
        }
    }

    suspend fun getMovieByCategory(category: Categories, list: ((List<MovieModel>) -> Unit)) {
        val listMovie = ArrayList<MovieModel>()
        db.collection(MOVIES)
            .whereArrayContains(CATEGORIES, category.name)
            .get()
            .addOnSuccessListener { documents ->
                for (movie in documents.toObjects(MovieModel::class.java)) {
                    listMovie.add(movie)
                    Timber.tag("moviesbycate").d("size" + listMovie.size)
                }
                list.invoke(listMovie)
            }
            .addOnFailureListener { exception ->
                Timber.tag(TAG).w(exception, "Error getting documents: ")
                list.invoke(emptyList())
            }
    }

/*    suspend fun getMovieByName(name: String, list: ((List<MovieModel>) -> Unit)) {
        val listMovie = ArrayList<MovieModel>()
        db.collection(MOVIES)
            .whereLessThan("name", name)
            .get()
            .addOnSuccessListener { documents ->
                for (movie in documents.toObjects(MovieModel::class.java)) {
                    listMovie.add(movie)
                    Timber.tag("moviesbycate").d("size" + listMovie.size)
                }
                list.invoke(listMovie)
            }
            .addOnFailureListener { exception ->
                Timber.tag(TAG).w(exception, "Error getting documents: ")
                list.invoke(emptyList())
            }
    }*/

}

