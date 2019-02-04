package com.example.activito.viewmodel

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class UserViewModel(application: Application): AndroidViewModel(application) {

    var currentUser:GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(getApplication())

    private val googleSignInOpetions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .build()

    private val googleSignInClient = GoogleSignIn.getClient(application, googleSignInOpetions);

    fun signInIntent(): Intent {
        return googleSignInClient.signInIntent
    }

    fun signOut(){
        googleSignInClient.signOut()
        currentUser = null
    }

}