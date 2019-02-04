package com.example.activito.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.activito.R
import com.example.activito.activity.LoginActivity
import com.example.activito.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    lateinit var userViewModel:UserViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logout_button.setOnClickListener{
            userViewModel.signOut()
            startActivity(Intent(context, LoginActivity::class.java))
            activity?.finish()
        }
    }


}
