package com.molo17.commons.android

import android.annotation.SuppressLint
import android.text.InputType
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.EditText

/**
 * Created by Damiano Giusti on 2019-06-06.
 */
class EditTextClickTouchListener(
    private val target: EditText
) : View.OnTouchListener, GestureDetector.SimpleOnGestureListener() {

    private val detector = GestureDetector(target.context.applicationContext, this)

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        detector.onTouchEvent(event)
        return true
    }

    override fun onDown(e: MotionEvent?): Boolean {
        target.alpha = 0.5F
        return true
    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        target.alpha = 1.0F
        target.performClick()
        return true
    }

    override fun onLongPress(e: MotionEvent?) {
        target.alpha = 1.0F
    }
}

fun EditText.setClickableOnly() {
    isFocusable = false
    isClickable = true
    inputType = InputType.TYPE_NULL
    setOnTouchListener(EditTextClickTouchListener(this))
}