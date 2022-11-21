package com.example.windmoiveapp.firebase

import android.util.Log
import com.example.windmoiveapp.constant.Categories
import com.example.windmoiveapp.model.MovieModel
import com.example.windmoiveapp.model.RatingModel
import com.example.windmoiveapp.model.UserModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import timber.log.Timber

object FireBaseService {
    private val db by lazy { Firebase.firestore }
    private val auth by lazy { Firebase.auth }
    private const val USERS = "users"
    private const val MOVIES = "movies"
    private const val RATINGS = "ratings"
    private const val CATEGORIES = "categories"
    private const val DISLIKE_NUM = "dislikeNum"
    private const val LIKE_NUM = "likeNum"
    private const val TAG = "Firebase"


    // <===========================================================================USER=====================================================================>

    fun addInfoUser(user: UserModel, onResult: ((Boolean) -> Unit)? = null) {
        db.collection(USERS).document(user.uid ?: "").set(user)
            .addOnSuccessListener {
                onResult?.invoke(true)
            }.addOnFailureListener {
                onResult?.invoke(false)
            }
    }

    fun updateInfoUser(
        uid: String,
        vararg fieldUser: Pair<String, Any>,
        onResult: ((Boolean) -> Unit)
    ) {
        db.collection(USERS).document(uid).update(fieldUser.toMap())
            .addOnCompleteListener {
                onResult.invoke(true)
            }.addOnFailureListener {
                onResult.invoke(false)
            }
    }

    fun getInfoAllUser(onResult: ((List<UserModel>) -> Unit)?) {
        val listUser = arrayListOf<UserModel>()
        db.collection(USERS).get().addOnSuccessListener { documents ->
            for (user in documents) {
                listUser.add(user.toObject())
            }
            onResult?.invoke(listUser)
        }.addOnFailureListener {
            onResult?.invoke(emptyList())
        }
    }

    fun getInfoUser(uid: String, onResult: ((UserModel?) -> Unit)?) {
        db.collection(USERS).document(uid).get().addOnSuccessListener { documentSnapshot ->
            onResult?.invoke(documentSnapshot.toObject())
        }.addOnFailureListener {
            onResult?.invoke(null)
        }
    }

    fun deleteInfoUser(uid: String, onResult: ((Boolean) -> Unit)?) {
        db.collection(USERS).document(uid).delete()
            .addOnSuccessListener {
                onResult?.invoke(true)
            }.addOnFailureListener {
                onResult?.invoke(false)
            }
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


    // <===========================================================================AUTHENTICATION=====================================================================>


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


    // <===========================================================================MOVIE=====================================================================>


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


    // <==========================RATINGS=====================================================================>

    suspend fun addRating(rating: RatingModel, onResult: ((Boolean) -> Unit)?) {
        db.collection(RATINGS).document(rating.id ?: "").set(rating)
            .addOnSuccessListener {
                onResult?.invoke(true)
            }.addOnFailureListener {
                onResult?.invoke(false)
            }

    }


    suspend fun getAllRating(listRating: ((List<RatingModel>) -> Unit)) {
        val list = arrayListOf<RatingModel>()
        db.collection(RATINGS).get().addOnSuccessListener { ratings ->
            for (rate in ratings) {
                list.add(rate.toObject())
            }
            listRating.invoke(list)
        }.addOnFailureListener {
            listRating.invoke(emptyList())
        }
    }

    fun deleteRating(rating: RatingModel, onResult: ((Boolean) -> Unit)?) {
        db.collection(RATINGS).document(rating.id ?: "").delete()
            .addOnSuccessListener {
                onResult?.invoke(true)
            }.addOnFailureListener {
                onResult?.invoke(false)
            }
    }


}

