package com.example.activito.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.ViewModelProviders
import com.example.activito.R
import com.example.activito.viewmodel.UserViewModel

class SplashScreenActivity : AppCompatActivity() {

    val ANIMATION_TIMER: Long = 3000;
    lateinit var splashScreenViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        splashScreenViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        val currentUser = splashScreenViewModel.currentUser
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
