package com.example.activito.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.activito.R
import com.example.activito.viewmodel.UserViewModel

class SplashScreenActivity : AppCompatActivity() {

    val ANIMATION_TIMER: Long = 3000;
    lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        val currentUser = userViewModel.currentUser
        Handler().postDelayed({
            if(currentUser == null){
                startActivity(Intent(this, LoginActivity::class.java))
            }else{
                startActivity(Intent(this, MainActivity::class.java))
            }
            finish()
        }, ANIMATION_TIMER)
    }
}
