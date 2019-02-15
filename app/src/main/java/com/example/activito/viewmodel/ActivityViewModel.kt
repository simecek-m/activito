package com.example.activito.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.fitness.FitnessActivities
import com.google.android.gms.fitness.data.DataSet
import com.google.android.gms.fitness.data.Field
import java.math.RoundingMode
import kotlin.math.roundToInt

class ActivityViewModel: ViewModel(){

    var steps:MutableLiveData<Int> = MutableLiveData()
    var calories:MutableLiveData<Int> = MutableLiveData()
    var distance:MutableLiveData<Float> = MutableLiveData()
    var moveMinutes:MutableLiveData<Int> = MutableLiveData()
    var runningTime:MutableLiveData<Int> = MutableLiveData()
    var bikingTime:MutableLiveData<Int> = MutableLiveData()

    val metersInKilometer = 1000

    fun setDailySteps(dataSet: DataSet){
        steps.value = dataSet.dataPoints
            ?.firstOrNull()
            ?.getValue(Field.FIELD_STEPS)
            ?.asInt()
    }

    fun setDailyCalories(dataSet: DataSet){
        calories.value = dataSet.dataPoints
            ?.firstOrNull()
            ?.getValue(Field.FIELD_CALORIES)
            ?.asFloat()?.roundToInt()
    }

    fun setDailyDistance(dataSet: DataSet){
        distance.value = dataSet.dataPoints
            ?.firstOrNull()
            ?.getValue(Field.FIELD_DISTANCE)
            ?.asFloat()
            ?.div(metersInKilometer)
            ?.toBigDecimal()
            ?.setScale(2, RoundingMode.UP)
            ?.toFloat()
    }

    fun setDailyMoveMinutes(dataSet: DataSet){
        moveMinutes.value = dataSet.dataPoints
            ?.firstOrNull()
            ?.getValue(Field.FIELD_DURATION)
            ?.asInt()
    }

    fun setDailyActivities(dataSet: DataSet) {
        dataSet.dataPoints.forEach {
            when(it.getValue(Field.FIELD_ACTIVITY).asActivity()){
                FitnessActivities.RUNNING -> runningTime.value = it.getValue(Field.FIELD_DURATION).asInt() / 1000 / 60
                FitnessActivities.BIKING -> bikingTime.value = it.getValue(Field.FIELD_DURATION).asInt() / 1000 / 60
            }
        }
    }
}