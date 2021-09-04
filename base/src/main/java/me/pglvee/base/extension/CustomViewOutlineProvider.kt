/*
 * Copyright (c) 2021. pinggonglve
 */

package me.pglvee.base.extension

import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider

/**
 * 裁剪view
 * 使用方式：
 * 1.view.setOutlineProvider(viewOutlineProvider) //把自定义的轮廓提供者设置给view
 * 2.view.setClipToOutline(true) //开启裁剪
 */
object CustomViewOutlineProvider {

    fun roundRect(radius: Float) = object : ViewOutlineProvider() {
        override fun getOutline(view: View, outline: Outline) {
            outline.setRoundRect(0, 0, view.width, view.height, radius)
        }
    }

    fun circle() = object : ViewOutlineProvider() {
        override fun getOutline(view: View, outline: Outline) {
            outline.setOval(0, 0, view.width, view.height)
        }
    }
}