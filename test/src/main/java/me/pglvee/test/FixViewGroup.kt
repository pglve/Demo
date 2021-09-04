/*
 * Copyright (c) 2021. pinggonglve
 */

package me.pglvee.test

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.FrameLayout

class FixViewGroup @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        Log.e("Touch", "FixViewGroup :: dispatchTouchEvent ${ev?.action}")
        return super.dispatchTouchEvent(ev)
    }
}