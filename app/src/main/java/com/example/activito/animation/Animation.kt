package com.example.activito.animation

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation

object Animation {

    private val ROTATION_ANIMATION_REPEAT = 3
    private val ROTATION_ANIMATION_DURATION = 1000L

    fun rotation(view: View){
        view.startAnimation(RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f).apply {
            repeatCount = ROTATION_ANIMATION_REPEAT
            duration = ROTATION_ANIMATION_DURATION
        })
    }

}