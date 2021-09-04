/*
 * Copyright (c) 2021. pinggonglve
 */

package me.pglvee.base.viewBinding

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RestrictTo
import androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP

/**
 * Utility to find root view for ViewBinding in Activity
 */
@RestrictTo(LIBRARY_GROUP)
object DefaultActivityViewBingingRootProvider {

    fun findRootView(activity: Activity): View {
        val contentView = activity.findViewById<ViewGroup>(android.R.id.content)
        checkNotNull(contentView) { "Activity has no content view" }
        return when (contentView.childCount) {
            1 -> contentView.getChildAt(0)
            0 -> error("Content view has no children. Provide root view explicitly")
            else -> error("More than one child view found in Activity content view")
        }
    }
}