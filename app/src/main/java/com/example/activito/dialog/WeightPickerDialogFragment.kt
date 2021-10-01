package com.example.activito.dialog

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.NumberPicker
import androidx.fragment.app.DialogFragment
import com.example.activito.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class WeightPickerDialogFragment: DialogFragment() {

    private val DECIMAL_VALUE = 10
    private val TAG = "LogoutDialogFragment"

    private var positiveButtonClickListener: (()->Unit)? = null
    var newWeightEntry:Float = 0f

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val numberPickerLayout = activity?.layoutInflater?.inflate(R.layout.weight_picker_dialog, null)
        val wholeNumberView = numberPickerLayout?.findViewById<NumberPicker>(R.id.weight_picker_whole)?.apply {
            minValue = 10
            maxValue = 100
            value = 60
        }
        val decimalNumberView = numberPickerLayout?.findViewById<NumberPicker>(R.id.weight_picker_decimal)?.apply {
            minValue = 0
            maxValue = 9
            value = 0
        }
        return MaterialAlertDialogBuilder(requireActivity())
            .setTitle(R.string.weight_dialog_title)
            .setView(numberPickerLayout)
            .setMessage(R.string.weight_dialog_description)
            .setPositiveButton(R.string.confirm) { _, _ ->
                if(decimalNumberView != null && wholeNumberView != null){
                    val whole = wholeNumberView.value
                    val decimal = decimalNumberView.value.toFloat().div(DECIMAL_VALUE)
                    newWeightEntry = whole + decimal
                    positiveButtonClickListener?.invoke()
                        ?: Log.e(TAG, "positiveButtonClickListener not implemented")
                }
            }
            .create()
    }

    fun addOnPositiveButtonClickListener(listener: () -> Unit){
        this.positiveButtonClickListener = listener
    }
}