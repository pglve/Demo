/*
 * Copyright (c) 2020. pinggonglve
 */

package me.pglvee.demo

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.MotionScene
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.Constraints
import me.pglvee.base.extension.dp

class BannerMotionLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MotionLayout(context, attrs, defStyleAttr) {

    private var mBannerTransition: MotionScene.Transition? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        createBannerViews(5)
        val scene = MotionScene(this)
        mBannerTransition = createTransition(scene)

        scene.addTransition(mBannerTransition)
        scene.setTransition(mBannerTransition)
        setScene(scene)
    }

    private fun createTransition(scene: MotionScene) : MotionScene.Transition {

        for(index in 0 until childCount) {
            val view = getChildAt(index)
            val lp = view.layoutParams as Constraints.LayoutParams
            lp.startToStart = Constraints.LayoutParams.PARENT_ID
            lp.endToEnd = Constraints.LayoutParams.PARENT_ID
            lp.topToTop = Constraints.LayoutParams.PARENT_ID
            lp.bottomToBottom = Constraints.LayoutParams.PARENT_ID
            view.layoutParams = lp

            val lView = getChildAt(if (index - 1 < 0) childCount - 1 else index -1)
            val lpl = view.layoutParams as Constraints.LayoutParams
            lpl.startToStart = Constraints.LayoutParams.PARENT_ID
            lpl.endToEnd = Constraints.LayoutParams.PARENT_ID
            lpl.topToTop = Constraints.LayoutParams.PARENT_ID
            lpl.bottomToBottom = Constraints.LayoutParams.PARENT_ID
            lpl.translationX = (-250).dp
            lpl.rotationY = -15f
            lView.layoutParams = lpl

            val rView = getChildAt(if (index - 1 < 0) childCount - 1 else index -1)
            val lpr = view.layoutParams as Constraints.LayoutParams
            lpr.startToStart = Constraints.LayoutParams.PARENT_ID
            lpr.endToEnd = Constraints.LayoutParams.PARENT_ID
            lpr.topToTop = Constraints.LayoutParams.PARENT_ID
            lpr.bottomToBottom = Constraints.LayoutParams.PARENT_ID
            lpr.transformPivotX = 250.dp
            lpr.rotationY = 15f
            rView.layoutParams = lpr
        }

        val startSetId = View.generateViewId()
        val startSet = getConstraintSet(startSetId)
        startSet.clone(this)

        val endSetId = View.generateViewId()
        val endSet = getConstraintSet(endSetId)
        val constraint = ConstraintSet.Constraint()
        endSet.clone(this)

        setTransition(mBannerTransition)

        return MotionScene.Transition(1, null, 1, 1)
    }

    @SuppressLint("InflateParams")
    private fun createBannerViews(size: Int) {
        for (i in 0 until size) {
            val view = LayoutInflater.from(context).inflate(R.layout.view_banner, null)
            view.id = View.generateViewId()
            addView(view)
        }
    }

    private fun testCreate() {
        val startSetId = View.generateViewId()
        val startSet = ConstraintSet()
        startSet.clone(this)

        val endSetId = View.generateViewId()
        val endSet = ConstraintSet()
        endSet.clone(this)
    }
}