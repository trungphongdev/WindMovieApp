package com.example.windmoiveapp.firebase

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import kotlinx.coroutines.launch

object GoogleService {
    fun loginWithAccountGg(account: GoogleSignInAccount?) {
    }

    fun isLoggedInGg(context: Context) {
            val account = GoogleSignIn.getLastSignedInAccount(context)
        }
    }
}