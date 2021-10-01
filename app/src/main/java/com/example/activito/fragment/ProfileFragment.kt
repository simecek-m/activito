package com.example.activito.fragment

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.activito.R
import com.example.activito.activity.LoginActivity
import com.example.activito.animation.Animation
import com.example.activito.databinding.FragmentProfileBinding
import com.example.activito.dialog.LogoutDialogFragment
import com.example.activito.module.GlideApp
import com.example.activito.viewmodel.UserViewModel
import com.google.android.gms.fitness.Fitness
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    private val LOGOUT_DIALOG_TAG = "logout_dialog"
    private val userViewModel:UserViewModel by viewModels()
    lateinit var binding:FragmentProfileBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        binding.viewmodel = userViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel.currentUser?.photoUrl?.let {
            GlideApp.with(this)
                .load(it)
                .placeholder(R.drawable.ic_avatar)
                .circleCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(profile_image)
        }

        logout_button.setOnClickListener{
            LogoutDialogFragment().let {
                it.addOnPositiveButtonClickListener{
                    userViewModel.signOut().addOnSuccessListener {
                        startActivity(Intent(context, LoginActivity::class.java))
                        activity?.finish()
                    }
                }
                it.show(fragmentManager!!, LOGOUT_DIALOG_TAG)
            }
        }

        loadUserBodyInfo()
        sync.setOnClickListener { selectedView ->
            if(view.animation == null){
                userViewModel.synchronizeFitService()
                Animation.rotation(selectedView)
                loadUserBodyInfo()
            }
        }

    }

    private fun loadUserBodyInfo(){
        Fitness.getHistoryClient(requireActivity(), userViewModel.currentUser!!)
            .readData(userViewModel.getBodyInfoRequest())
            .addOnSuccessListener { response ->
                userViewModel.setBodyInfo(response)
            }.addOnFailureListener{ e -> Log.e(TAG, "bodyInfoRequest", e)}
    }
}
