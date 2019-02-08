package com.example.activito.viewmodel

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.activito.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.Scope
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.data.Field
import com.google.android.gms.fitness.request.DataReadRequest
import com.google.android.gms.fitness.result.DataReadResponse
import java.util.*
import java.util.concurrent.TimeUnit

class UserViewModel(application: Application): AndroidViewModel(application) {

    var currentUser: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(getApplication())

    var height: MutableLiveData<Float> = MutableLiveData()
    var weight:MutableLiveData<Float> = MutableLiveData()


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

    fun getBodyInfoRequest():DataReadRequest{
        val calendar = Calendar.getInstance()
        calendar.time = Date()

        return DataReadRequest.Builder()
            .read(DataType.TYPE_HEIGHT)
            .read(DataType.TYPE_WEIGHT)
            .setTimeRange(1, calendar.timeInMillis, TimeUnit.MILLISECONDS)
            .setLimit(1)
            .build()
    }

    fun setBodyInfo(response: DataReadResponse) {
        weight.value = response.getDataSet(DataType.TYPE_WEIGHT)
            .dataPoints.lastOrNull()
            ?.getValue(Field.FIELD_WEIGHT)
            ?.asFloat()

        height.value = response.getDataSet(DataType.TYPE_HEIGHT)
            .dataPoints.lastOrNull()
            ?.getValue(Field.FIELD_HEIGHT)
            ?.asFloat()
    }

    fun getWeightProgressRequest():DataReadRequest{
        val calendar = Calendar.getInstance()
        calendar.time = Date()

        val endTime = calendar.timeInMillis
        calendar.add(Calendar.MONTH, -6)
        val startTime = calendar.timeInMillis

        return DataReadRequest.Builder()
            .read(DataType.TYPE_WEIGHT)
            .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
            .build()
    }

}