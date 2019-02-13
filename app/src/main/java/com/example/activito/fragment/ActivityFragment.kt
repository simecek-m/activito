package com.example.activito.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.activito.R
import com.example.activito.databinding.FragmentActivityBinding
import com.example.activito.viewmodel.ActivityViewModel
import com.example.activito.viewmodel.UserViewModel
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.data.DataType

class ActivityFragment : Fragment() {

    private val TAG = "ActivityFragment"
    lateinit var userViewModel: UserViewModel
    lateinit var activityViewModel: ActivityViewModel
    lateinit var binding: FragmentActivityBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        activityViewModel = ViewModelProviders.of(this).get(ActivityViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_activity, container, false)
        binding.viewmodel = activityViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadDailyCalories()
        loadDailySteps()
    }

    fun loadDailySteps(){
        Fitness.getHistoryClient(activity!!, userViewModel.currentUser!!)
            .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
            .addOnSuccessListener { dataSet ->
                activityViewModel.setDailySteps(dataSet)
            }.addOnFailureListener{ e -> Log.e(TAG, "loadDailySteps", e)}

    }

    fun loadDailyCalories(){
        Fitness.getHistoryClient(activity!!, userViewModel.currentUser!!)
            .readDailyTotal(DataType.TYPE_CALORIES_EXPENDED)
            .addOnSuccessListener { dataSet ->
                activityViewModel.setDailyCalories(dataSet)
            }.addOnFailureListener{ e -> Log.e(TAG, "loadDailyCalories", e)}
    }
}
