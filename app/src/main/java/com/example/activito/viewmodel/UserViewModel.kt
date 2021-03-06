package com.example.activito.viewmodel

import android.accounts.AccountManager
import android.app.Application
import android.content.ContentResolver
import android.content.Intent
import android.content.SyncRequest
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.github.mikephil.charting.data.Entry
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.Scope
import com.google.android.gms.fitness.data.DataSet
import com.google.android.gms.fitness.data.DataSource
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.data.Field
import com.google.android.gms.fitness.request.DataReadRequest
import com.google.android.gms.fitness.result.DataReadResponse
import com.google.android.gms.tasks.Task
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


class UserViewModel(application: Application): AndroidViewModel(application) {

    private val GOOGLE_FIT_SYNC_AUTHORITY = "com.google.android.gms.fitness"
    private val GOOGLE_ACCOUNT_TYPE = "com.google"

    var currentUser: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(getApplication())
    var height: MutableLiveData<Float> = MutableLiveData()
    var weight:MutableLiveData<Float> = MutableLiveData()

    val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .requestProfile()
        .requestScopes(Scope(Scopes.FITNESS_BODY_READ_WRITE))
        .requestScopes(Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
        .requestScopes(Scope(Scopes.FITNESS_LOCATION_READ))
        .build()

    private val googleSignInClient = GoogleSignIn.getClient(application, googleSignInOptions);

    fun signInIntent(): Intent {
        return googleSignInClient.signInIntent
    }

    fun signOut(): Task<Void>{
        currentUser = null
        return googleSignInClient.signOut()
    }

    fun getAddWeightRequest(weight: Float):DataSet{
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        val now = calendar.timeInMillis
        val dataSource = DataSource.Builder()
            .setAppPackageName(getApplication<Application>())
            .setDataType(DataType.TYPE_WEIGHT)
            .setType(DataSource.TYPE_RAW)
            .build()
        val dataSet = DataSet.create(dataSource)
        val dataPoint = dataSet.createDataPoint().setTimeInterval(now, now, TimeUnit.MILLISECONDS)
        dataPoint.getValue(Field.FIELD_WEIGHT).setFloat(weight)
        dataSet.add(dataPoint)
        return dataSet
    }

    fun getBodyInfoRequest():DataReadRequest{
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        val endTime = calendar.timeInMillis

        return DataReadRequest.Builder()
            .enableServerQueries()
            .read(DataType.TYPE_WEIGHT)
            .read(DataType.TYPE_HEIGHT)
            .setTimeRange(1, endTime, TimeUnit.MILLISECONDS)
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
        val now = calendar.timeInMillis
        return DataReadRequest.Builder()
            .enableServerQueries()
            .read(DataType.TYPE_WEIGHT)
            .setTimeRange(1, now, TimeUnit.MILLISECONDS)
            .setLimit(5)
            .build()
    }

    fun getWeightProgressChartPoints(data:DataReadResponse): ArrayList<Entry>{
        val result = ArrayList<Entry>()
        data.getDataSet(DataType.TYPE_WEIGHT).dataPoints.forEach {
            result.add(Entry( it.getStartTime(TimeUnit.MILLISECONDS).toFloat() , it.getValue(Field.FIELD_WEIGHT).asFloat()))
        }
        return result
    }

    fun synchronizeFitService(){
        AccountManager.get(getApplication()).getAccountsByType(GOOGLE_ACCOUNT_TYPE)
            .forEach { account ->
                if(account.name == GoogleSignIn.getLastSignedInAccount(getApplication())?.email){
                    ContentResolver.setSyncAutomatically(account, GOOGLE_FIT_SYNC_AUTHORITY, true)
                    val syncRequest = SyncRequest.Builder()
                        .setSyncAdapter(account, GOOGLE_FIT_SYNC_AUTHORITY)
                        .setExtras(Bundle())
                        .syncOnce()
                        .build()
                    ContentResolver.requestSync(syncRequest)
                }
            }
    }
}