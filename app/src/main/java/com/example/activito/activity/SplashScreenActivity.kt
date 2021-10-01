package com.example.activito.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.activito.R
import com.example.activito.viewmodel.UserViewModel

class SplashScreenActivity : AppCompatActivity() {

    val ANIMATION_TIMER: Long = 3000;
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
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
