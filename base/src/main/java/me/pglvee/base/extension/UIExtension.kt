/*
 * Copyright (c) 2021. pinggonglve
 */

package me.pglvee.base.extension

import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.annotation.StringRes

/** 高亮文本 */
fun TextView.highlight(all: CharSequence, part: String, color: Int) {
    val sp = SpannableString(all)
    val start = all.indexOf(part)
    sp.setSpan(ForegroundColorSpan(color), start, start + part.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    text = sp
}

/** 高亮文本 */
fun TextView.highlight(@StringRes res: Int, arg: Any, color: Int) {
    val all = context.getString(res, arg)
    val part = arg.toString()
    val sp = SpannableString(all)
    val start = all.indexOf(part)
    sp.setSpan(ForegroundColorSpan(color), start, start + part.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    text = sp
}