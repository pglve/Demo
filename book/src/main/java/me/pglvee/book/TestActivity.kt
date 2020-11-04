/*
 * Copyright (c) 2020. pinggonglve
 */

package me.pglvee.book

import android.graphics.Color
import android.opengl.GLSurfaceView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class TestActivity : AppCompatActivity() {

    private val mGLSurfaceView: GLSurfaceView by lazy { GLSurfaceView(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mGLSurfaceView)
        //设置版本
        mGLSurfaceView.setEGLContextClientVersion(3)

        val renderer = TestRenderer(Color.DKGRAY)

        mGLSurfaceView.setRenderer(renderer)
    }
}