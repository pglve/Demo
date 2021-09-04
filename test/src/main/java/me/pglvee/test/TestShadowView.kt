/*
 * Copyright (c) 2021. pinggonglve
 */

package me.pglvee.test

import android.content.Context
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class TestShadowView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val mPaint = Paint()

    init {
        mPaint.color = Color.RED
        mPaint.maskFilter = BlurMaskFilter(30f, BlurMaskFilter.Blur.OUTER)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawRoundRect(200f, 200f, 600f, 600f, 20f, 20f, mPaint)
    }
}