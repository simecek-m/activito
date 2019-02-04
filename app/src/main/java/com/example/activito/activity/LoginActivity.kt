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
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    private val TAG = "LoginActivity"
    private val GOOGLE_SIGN_IN_REQUEST_CODE = 1
    private lateinit var userViewModel:UserViewModel

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
            task.addOnSuccessListener{startActivity(Intent(this, MainActivity::class.java))}
                .addOnFailureListener{exception -> Log.e(TAG, "sign in failed:", exception)}
        }
    }
}
