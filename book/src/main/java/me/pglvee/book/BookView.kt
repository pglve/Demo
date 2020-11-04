/*
 * Copyright (c) 2020. pinggonglve
 */

package me.pglvee.book

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.view.KeyEvent
import androidx.compose.ui.graphics.VertexMode

class BookView(context: Context?, attrs: AttributeSet?) : GLSurfaceView(context, attrs) {

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
            queueEvent {

            }
        }
        return super.onKeyDown(keyCode, event)
    }

}