package com.example.activito.fragment

import android.content.ContentValues.TAG
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.activito.R
import com.example.activito.animation.Animation
import com.example.activito.viewmodel.UserViewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.gms.fitness.Fitness
import kotlinx.android.synthetic.main.fragment_weight.*

class WeightFragment : Fragment() {

    lateinit var userViewModel: UserViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        return inflater.inflate(R.layout.fragment_weight, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        add_weight.setOnClickListener { addWeight() }
        sync.setOnClickListener { selectedView ->
            if(view.animation == null){
                userViewModel.synchronizeFitService()
                Animation.rotation(selectedView)
                loadWeightProgress()
            }
        }
        loadWeightProgress()
    }

    private fun addWeight() {
        Toast.makeText(activity!!, getString(R.string.feature_not_supported_yet), Toast.LENGTH_LONG).show()
    }

    private fun loadWeightProgress(){
        Fitness.getHistoryClient(activity!!, userViewModel.currentUser!!)
            .readData(userViewModel.getWeightProgressRequest())
            .addOnSuccessListener { response ->
                if (isAdded){
                    val entries = userViewModel.getWeightProgressChartPoints(response)
                    val dataSet = LineDataSet(entries, "leden")
                    val primaryColor = ContextCompat.getColor(activity!!, R.color.colorPrimary)
                    dataSet.apply {
                        setCircleColor(Color.WHITE)
                        circleHoleColor = primaryColor
                        color = primaryColor
                        setDrawFilled(true)
                        fillColor = primaryColor
                        fillAlpha = 255
                        lineWidth = 3f
                        circleRadius = 10f
                        circleHoleRadius = 5f
                        valueTextSize = 20f
                        valueTextColor = primaryColor
                        mode = LineDataSet.Mode.HORIZONTAL_BEZIER
                    }

                    (chart as LineChart).apply {
                        setTouchEnabled(false)
                        data = LineData(dataSet)
                        description = null
                        xAxis.isEnabled = false
                        axisRight.isEnabled = false
                        axisLeft.isEnabled = false
                        invalidate()
                        setNoDataText(getString(R.string.loading_weight_data))
                        setNoDataTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
                    }
                }
            }.addOnFailureListener{e -> Log.e(TAG, "weight progress load failure: ", e)}
    }
}
