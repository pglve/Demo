/*
 * Copyright (c) 2020. pinggonglve
 */

package me.pglvee.compress

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.util.Log
import me.pglvee.compress.ImageUtil.toByteArray
import me.pglvee.compress.LibLoader.imageCompress
import me.pglvee.compress.LibLoader.thumbnailCompress
import java.io.File
import java.io.FileOutputStream

object CompressUtil {

    private const val TAG = "compress"
    private const val MIN_SIZE = 200*1024L

    fun newInstance(context: Context, minSize: Long = MIN_SIZE): ImageData {
        return ImageData(context, minSize)
    }

    @Synchronized
    fun ImageData.compress(
        inDimension: ((IntArray) -> Unit)? = null,
        outDimension: ((IntArray) -> Unit)? = null,
        outByteArray: ((ByteArray) -> Unit)? = null
    ) {
        var scale = 1f
        var cropOptions = intArrayOf(0, 0, 0, 0)

        var bitmap: Bitmap = inputBitmap?.let {
            cropOptions = ImageUtil.getOptionCrop(it.width, it.height, maxScale)
            scale = ImageUtil.getOptionScale(cropOptions[0], cropOptions[1], width, height)
            it.copy(Bitmap.Config.ARGB_8888, true)
        }?: kotlin.run {
            val newOpts = BitmapFactory.Options()
            newOpts.inJustDecodeBounds = true
            newOpts.inPreferredConfig = Bitmap.Config.ARGB_8888
            BitmapFactory.decodeFile(inputFilePath, newOpts)
            newOpts.inJustDecodeBounds = false
            cropOptions = ImageUtil.getOptionCrop(newOpts.outWidth, newOpts.outHeight, maxScale)
            scale = ImageUtil.getOptionScale(cropOptions[0], cropOptions[1], width, height)
            newOpts.inSampleSize = ImageUtil.getOptionSample(scale)
            BitmapFactory.decodeFile(inputFilePath, newOpts)
        } ?: return

        inputFilePath?.takeIf { File(it).length() < minSize }?.let { input ->
            if (maxScale == 0f && scale == 1f) {
                inDimension?.invoke(intArrayOf(bitmap.width, bitmap.height))
                outDimension?.invoke(intArrayOf(bitmap.width, bitmap.height))
                outByteArray?.invoke(File(input).toByteArray())
                outputFilePath?.let { output ->
                    File(input).copyTo(File(output), true)
                }
                return
            }
        }

        Log.d(TAG, "input Image: ${bitmap.width} x ${bitmap.height}, bitmap size: ${bitmap.allocationByteCount}")
        inDimension?.invoke(intArrayOf(bitmap.width, bitmap.height))
        val outWidth = (bitmap.width / scale).toInt()
        val outHeight = (bitmap.height / scale).toInt()
        if (scale > 1) bitmap = Bitmap.createScaledBitmap(bitmap, outWidth, outHeight, true)
        if (maxScale > 0) bitmap = Bitmap.createBitmap(bitmap,
            (cropOptions[2] / scale).toInt(),
            (cropOptions[3] / scale).toInt(),
            (cropOptions[0] / scale).toInt(),
            (cropOptions[1] / scale).toInt()
        )
        if (angle > 0) {
            val matrix = Matrix()
            matrix.setRotate(
                angle.toFloat(),
                bitmap.width.toFloat() / 2,
                bitmap.height.toFloat() / 2
            )
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        }
        Log.d(TAG, "output Image: ${bitmap.width} x ${bitmap.height}, bitmap size: ${bitmap.allocationByteCount}")
        outDimension?.invoke(intArrayOf(bitmap.width, bitmap.height))

        if (background != 0) {
            val canvas = Canvas(bitmap)
            canvas.drawColor(background)
            canvas.drawBitmap(bitmap, 0f, 0f, null)
        }
        val beforeFile: String = ImageUtil.getTempFile(context)
        FileOutputStream(beforeFile).use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 95, it)
            it.flush()
        }
        bitmap.recycle()
        Log.d(TAG, "first compress by quality 100, input file size: ${File(beforeFile).length()}")
        val afterFile: String = ImageUtil.getTempFile(context)
        if (maxSize > 0)
            thumbnailCompress(beforeFile, outputFilePath ?: afterFile, maxSize)
        else if (quality > 0)
            imageCompress(beforeFile, outputFilePath ?: afterFile, quality)
        Log.d(TAG, "second compress, out file size: ${File(outputFilePath ?: afterFile).length()}")
        File(beforeFile).delete()
        outByteArray?.invoke(File(outputFilePath ?: afterFile).toByteArray())
        File(afterFile).delete()
    }
}