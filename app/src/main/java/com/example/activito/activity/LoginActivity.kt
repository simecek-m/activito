package com.example.activito.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.activito.R
import com.example.activito.viewmodel.UserViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    private val TAG = "LoginActivity"
    private val GOOGLE_SIGN_IN_REQUEST_CODE = 1
    private lateinit var userViewModel:UserViewModel

    private val subscriptionTypes = listOf<DataType>(
        DataType.TYPE_WEIGHT,
        DataType.TYPE_HEIGHT,
        DataType.TYPE_STEP_COUNT_DELTA,
        DataType.TYPE_CALORIES_EXPENDED
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        login_button.setOnClickListener {
            val intent = userViewModel.signInIntent()
            startActivityForResult(intent, GOOGLE_SIGN_IN_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == GOOGLE_SIGN_IN_REQUEST_CODE){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            task.addOnSuccessListener{
                handleLogin()
            }.addOnFailureListener{exception -> Log.e(TAG, "sign in failed:", exception)}
        }
    }

    fun handleLogin(){
        // subscribe data
        val tasks = ArrayList<Task<Void>>()
        val account = GoogleSignIn.getLastSignedInAccount(this)!!
        subscriptionTypes.forEach { dataType ->
            tasks.add(Fitness.getRecordingClient(this, account)
                .subscribe(dataType))
        }
        // synchronize data
        Tasks.whenAllComplete(tasks)
            .addOnSuccessListener {
                userViewModel.synchronizeFitService()
            }
        // redirect
        startActivity(Intent(this, MainActivity::class.java))
    }
}
