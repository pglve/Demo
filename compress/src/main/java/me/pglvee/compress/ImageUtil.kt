/*
 * Copyright (c) 2020. pinggonglve
 */

package me.pglvee.compress

import android.content.Context
import android.text.TextUtils
import androidx.exifinterface.media.ExifInterface
import java.io.*

object ImageUtil {

    fun getOptionSample(scale: Float): Int {
        return if (scale < 2) 1
        else if (scale >= 2f && scale < 4f) 2
        else if (scale >= 4f && scale < 8f) 4
        else if (scale >= 8f && scale < 16) 8
        else 16
    }

    fun getOptionScale(oW: Int, oH: Int, w: Int, h: Int): Float {
        if (w == 0 || h == 0 || oW == 0 || oH == 0) return 1f
        val ol = oW.coerceAtLeast(oH)
        val s = w.coerceAtLeast(h)
        return if (ol <= s) 1f else ol.toFloat() / s.toFloat()
    }

    fun getOptionCrop(oW: Int, oH: Int, maxScale: Float): IntArray {
        if (oW == 0 || oH == 0) return intArrayOf(0, 0, 0, 0)
        var oWC = oW
        var oHC = oH
        var xOffset = 0
        var yOffset = 0
        if (maxScale > 0) {
            if (oW.toFloat() / oH > maxScale) {
                oWC = (oH * maxScale).toInt()
                xOffset = (oW - oWC) / 2
            } else if (oH.toFloat() / oW > maxScale) {
                oHC = (oW * maxScale).toInt()
                yOffset = (oH - oHC) / 2
            }
        }
        return intArrayOf(oWC, oHC, xOffset, yOffset)
    }

    fun getTempFile(context: Context): String {
        val file =
            File.createTempFile("temp${System.currentTimeMillis()}", ".jpg", context.cacheDir)
        return file.absolutePath
    }

    fun readPictureDegree(path: String?): Int {
        if (TextUtils.isEmpty(path)) return 0
        var degree = 0
        try {
            val exifInterface = ExifInterface(path!!)
            val orientation = exifInterface.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
            degree = when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90
                ExifInterface.ORIENTATION_ROTATE_180 -> 180
                ExifInterface.ORIENTATION_ROTATE_270 -> 270
                else -> 0
            }
        } catch (e: IOException) {
        }
        return degree
    }

    fun File.toByteArray(): ByteArray {
        FileInputStream(this).use { fis ->
            ByteArrayOutputStream().use { bos ->
                val b = ByteArray(8 * 1024)
                var n: Int
                while (fis.read(b).also { n = it } != -1) {
                    bos.write(b, 0, n)
                }
                return bos.toByteArray()
            }
        }
    }

    fun ByteArray.toPath(context: Context): String {
        val file = getTempFile(context)
        FileOutputStream(file).use { fos ->
            BufferedOutputStream(fos).use { bos ->
                bos.write(this)
            }
        }
        return file
    }

}