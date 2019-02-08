package com.example.activito.fragment

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.example.activito.R
import com.example.activito.viewmodel.UserViewModel
import com.google.android.gms.fitness.Fitness

class WeightFragment : Fragment() {

    lateinit var userViewModel: UserViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_weight, container, false)
        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Fitness.getHistoryClient(activity!!, userViewModel.currentUser!!)
            .readData(userViewModel.getWeightProgressRequest())
            .addOnSuccessListener { response ->
                // todo: show weight progress graph
                println(response.dataSets)
            }
            .addOnFailureListener{ e -> Log.e(TAG, "weight progress load failure: ", e)}
    }



}
