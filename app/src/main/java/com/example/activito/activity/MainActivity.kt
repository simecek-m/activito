package com.example.activito.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.activito.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.data.DataType
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navController = findNavController(R.id.nav_host_fragment)
        bottom_navigation.setupWithNavController(navController)
        subscribeDataTypes()
    }

    fun subscribeDataTypes(){
        Fitness.getRecordingClient(this, GoogleSignIn.getLastSignedInAccount(this)!!)
            .subscribe(DataType.TYPE_WEIGHT)

        Fitness.getRecordingClient(this, GoogleSignIn.getLastSignedInAccount(this)!!)
            .subscribe(DataType.TYPE_HEIGHT)
    }
}
