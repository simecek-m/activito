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
import com.example.activito.animation.Animation
import com.example.activito.databinding.FragmentActivityBinding
import com.example.activito.viewmodel.ActivityViewModel
import com.example.activito.viewmodel.UserViewModel
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.data.DataType
import kotlinx.android.synthetic.main.fragment_activity.*

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
        loadDailyDistance()
        loadDailyMoveMinutes()

        sync.setOnClickListener { clickedView ->
            if(view.animation == null){
                userViewModel.synchronizeFitService()
                Animation.rotation(clickedView)
                loadDailyCalories()
                loadDailySteps()
                loadDailyDistance()
                loadDailyMoveMinutes()
            }
        }
    }

    private fun loadDailySteps(){
        Fitness.getHistoryClient(activity!!, userViewModel.currentUser!!)
            .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
            .addOnSuccessListener{ activityViewModel.setDailySteps(it)}
            .addOnFailureListener{ e -> Log.e(TAG, "loadDailySteps", e)}
    }

    private fun loadDailyCalories(){
        Fitness.getHistoryClient(activity!!, userViewModel.currentUser!!)
            .readDailyTotal(DataType.TYPE_CALORIES_EXPENDED)
            .addOnSuccessListener{ activityViewModel.setDailyCalories(it)}
            .addOnFailureListener{ e -> Log.e(TAG, "loadDailyCalories", e)}
    }

    private fun loadDailyDistance(){
        Fitness.getHistoryClient(activity!!, userViewModel.currentUser!!)
            .readDailyTotal(DataType.TYPE_DISTANCE_DELTA)
            .addOnSuccessListener{ activityViewModel.setDailyDistance(it)}
            .addOnFailureListener{ e -> Log.e(TAG, "loadDailyDistance", e)}
    }

    private fun loadDailyMoveMinutes(){
        Fitness.getHistoryClient(activity!!, userViewModel.currentUser!!)
            .readDailyTotal(DataType.TYPE_MOVE_MINUTES)
            .addOnSuccessListener{ activityViewModel.setDailyMoveMinutes(it) }
            .addOnFailureListener{ e -> Log.e(TAG, "loadDailyMoveMinutes", e)}
    }
}
