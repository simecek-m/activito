package com.example.activito.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.activito.R
import com.example.activito.activity.LoginActivity
import com.example.activito.databinding.FragmentProfileBinding
import com.example.activito.module.GlideApp
import com.example.activito.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    lateinit var userViewModel:UserViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        val binding = DataBindingUtil.inflate<FragmentProfileBinding>(inflater, R.layout.fragment_profile, container, false)
        binding.viewmodel = userViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        GlideApp.with(this)
            .load(userViewModel.currentUser?.photoUrl)
            .placeholder(R.drawable.ic_avatar)
            .circleCrop()
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(profile_image)
        logout_button.setOnClickListener{
            userViewModel.signOut()
            startActivity(Intent(context, LoginActivity::class.java))
            activity?.finish()
        }
    }


}
