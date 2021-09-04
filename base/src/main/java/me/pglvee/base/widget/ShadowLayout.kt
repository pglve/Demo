/*
 * Copyright (c) 2021. pinggonglve
 */

package me.pglvee.base.widget

import android.content.Context
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import me.pglvee.base.extension.dp


class ShadowLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var mShadowColor = Color.parseColor("#10000000")
    private var mBackgroundColor = Color.WHITE
    private var mShadowRadius = 10f
    private var mShadowCorner = 5.dp
    private val blurMaskFilter = BlurMaskFilter(mShadowRadius, BlurMaskFilter.Blur.OUTER)

    private var mPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        mPaint.style = Paint.Style.FILL
    }

    override fun dispatchDraw(canvas: Canvas?) {
        mPaint.color = mShadowColor
        mPaint.maskFilter = blurMaskFilter
        canvas?.drawRoundRect(0f, 0f, width.toFloat(), height.toFloat(),
            mShadowCorner, mShadowCorner, mPaint)
        mPaint.color = mBackgroundColor
        mPaint.maskFilter = null
        canvas?.drawRoundRect(0f, 0f, width.toFloat(), height.toFloat(),
            mShadowCorner, mShadowCorner, mPaint)
        super.dispatchDraw(canvas)
    }
}