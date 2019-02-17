package com.example.activito.fragment

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.activito.R
import com.example.activito.animation.Animation
import com.example.activito.dialog.WeightPickerDialogFragment
import com.example.activito.viewmodel.UserViewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.gms.fitness.Fitness
import kotlinx.android.synthetic.main.fragment_weight.*
import java.text.SimpleDateFormat

class WeightFragment : Fragment() {

    private val WEIGHT_PICKER_DIALOG_TAG = "weight_picker_dialog"
    lateinit var userViewModel: UserViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        return inflater.inflate(R.layout.fragment_weight, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        add_weight.setOnClickListener { showAddWeightDialog() }
        sync.setOnClickListener { selectedView ->
            if(view.animation == null){
                userViewModel.synchronizeFitService()
                Animation.rotation(selectedView)
                loadWeightProgress()
            }
        }
        loadWeightProgress()
    }

    private fun showAddWeightDialog() {
        val dialog = WeightPickerDialogFragment()
        dialog.addOnPositiveButtonClickListener {
            Fitness.getHistoryClient(activity!!, userViewModel.currentUser!!)
                .insertData(userViewModel.getAddWeightRequest(dialog.newWeightEntry))
                .addOnSuccessListener { loadWeightProgress() }
        }
        dialog.show(fragmentManager!!, WEIGHT_PICKER_DIALOG_TAG)
    }

    private fun loadWeightProgress(){
        Fitness.getHistoryClient(activity!!, userViewModel.currentUser!!)
            .readData(userViewModel.getWeightProgressRequest())
            .addOnSuccessListener { response ->
                if (isAdded){
                    val entries = userViewModel.getWeightProgressChartPoints(response)
                    val dataSet = initDataSet(entries)
                    initLineChart(chart)
                    chart.data = LineData(dataSet)
                }
            }.addOnFailureListener{e -> Log.e(TAG, "weight progress load failure: ", e)}
    }

    private fun initDataSet(entries: List<Entry>):LineDataSet{
        val primaryColor = ContextCompat.getColor(activity!!, R.color.colorPrimary)
        val dataSet = LineDataSet(entries, "")
        return dataSet.apply {
            setCircleColor(primaryColor)
            circleHoleColor = primaryColor
            color = primaryColor
            lineWidth = 3f
            circleRadius = 5f
            circleHoleRadius = 3f
            setDrawHighlightIndicators(false)
            setDrawValues(false)
            valueTextColor = primaryColor
            mode = LineDataSet.Mode.HORIZONTAL_BEZIER
        }
    }

    private fun initLineChart(chart: LineChart){
        chart.apply {
            setTouchEnabled(true)
            invalidate()
            setDrawMarkers(true)
            xAxis.setDrawAxisLine(true)
            xAxis.setDrawGridLines(false)
            legend.isEnabled = false
            description = null
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setValueFormatter { value, _ ->  SimpleDateFormat("dd MMM").format(value)}
            axisLeft.setDrawGridLines(false)
            axisRight.setDrawAxisLine(false)
            axisRight.setDrawLabels(false)
            axisRight.setDrawGridLines(false)
            setNoDataText(getString(R.string.no_weight_data_available))
            setNoDataTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
        }
    }
}
