/*
 * Copyright (c) 2021. pinggonglve
 */

package me.pglvee.base.extension

import android.content.res.Resources
import android.util.TypedValue

val Int.dp: Float
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), Resources.getSystem().displayMetrics)
val Int.sp: Float
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this.toFloat(), Resources.getSystem().displayMetrics)
val screenWidth
    get() = Resources.getSystem().displayMetrics.widthPixels
val screenHeight
    get() = Resources.getSystem().displayMetrics.heightPixels