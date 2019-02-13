package com.example.activito.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.fitness.data.DataSet
import com.google.android.gms.fitness.data.Field
import kotlin.math.roundToInt

class ActivityViewModel: ViewModel(){

    var steps:MutableLiveData<Int> = MutableLiveData()
    var calories:MutableLiveData<Int> = MutableLiveData()

    fun setDailySteps(dataSet: DataSet){
        if(dataSet.isEmpty){
            steps.value = 0
        }else{
            steps.value = dataSet.dataPoints
                .firstOrNull()
                ?.getValue(Field.FIELD_STEPS)
                ?.asInt()
        }
    }

    fun setDailyCalories(dataSet: DataSet){
        if(dataSet.isEmpty){
            calories.value = 0
        }else{
            calories.value = dataSet.dataPoints
                .firstOrNull()
                ?.getValue(Field.FIELD_CALORIES)
                ?.asFloat()?.roundToInt()
        }
    }
}