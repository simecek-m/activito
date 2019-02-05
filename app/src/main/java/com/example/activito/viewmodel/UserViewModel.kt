package com.example.activito.viewmodel

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.Scope

class UserViewModel(application: Application): AndroidViewModel(application) {

    var currentUser:GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(getApplication())

    private val googleSignInOpetions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .requestScopes(Scope(Scopes.FITNESS_BODY_READ_WRITE))
        .requestScopes(Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
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