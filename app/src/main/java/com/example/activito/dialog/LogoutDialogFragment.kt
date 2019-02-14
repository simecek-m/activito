package com.example.activito.dialog

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.activito.R

class LogoutDialogFragment: DialogFragment() {

    private val TAG = "LogoutDialogFragment"
    private var positiveButtonClickListener: (()->Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let{
            AlertDialog.Builder(it)
                .setTitle(R.string.logout_dialog_title)
                .setMessage(R.string.logout_dialog_description)
                .setNegativeButton(R.string.cancel) { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton(R.string.logout) { _, _ ->
                    positiveButtonClickListener?.invoke()
                        ?: Log.e(TAG, "positiveButtonClickListener not implemented")
                }
                .create()
        }?: throw IllegalStateException("Activity cannot be null")
    }

    fun addOnPositiveButtonClickListener(listener: () -> Unit){
        this.positiveButtonClickListener = listener
    }
}