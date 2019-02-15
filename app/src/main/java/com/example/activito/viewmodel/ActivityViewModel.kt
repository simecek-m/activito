package com.example.activito.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.fitness.data.DataSet
import com.google.android.gms.fitness.data.Field
import java.math.RoundingMode
import kotlin.math.roundToInt

class ActivityViewModel: ViewModel(){

    var steps:MutableLiveData<Int> = MutableLiveData()
    var calories:MutableLiveData<Int> = MutableLiveData()
    var distance:MutableLiveData<Float> = MutableLiveData()
    var moveMinutes:MutableLiveData<Int> = MutableLiveData()

    val metersInKilometer = 1000

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

    fun setDailyDistance(dataSet: DataSet){
        if(dataSet.isEmpty){
            distance.value = 0f
        }else{
            distance.value = dataSet.dataPoints
                .firstOrNull()
                ?.getValue(Field.FIELD_DISTANCE)
                ?.asFloat()
                ?.div(metersInKilometer)
                ?.toBigDecimal()
                ?.setScale(2, RoundingMode.UP)
                ?.toFloat()
        }
    }

    fun setDailyMoveMinutes(dataSet: DataSet){
        if(dataSet.isEmpty){
            moveMinutes.value = 0
        }else{
            moveMinutes.value = dataSet.dataPoints
                .firstOrNull()
                ?.getValue(Field.FIELD_DURATION)
                ?.asInt()
        }
    }
}