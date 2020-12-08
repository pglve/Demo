/*
 * Copyright (c) 2020. pinggonglve
 */

package me.pglvee.compress

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import me.pglvee.compress.LibLoader.imageCompress
import me.pglvee.compress.LibLoader.thumbnailCompress
import java.io.*

object CompressUtil {

    fun newInstance(context: Context): ImageData {
        return ImageData(context)
    }

    @Synchronized
    fun ImageData.compress(
        inDimension: ((IntArray) -> Unit)? = null,
        outDimension: ((IntArray) -> Unit)? = null,
        outByteArray: ((ByteArray) -> Unit)? = null
    ) {
        val w: Int
        val h: Int
        val scale: Float
        val cropOptions: IntArray
        var recycle = false
        if (bitmap == null) {
            recycle = true
            val newOpts = BitmapFactory.Options()
            newOpts.inJustDecodeBounds = true
            newOpts.inPreferredConfig = Bitmap.Config.ARGB_8888
            BitmapFactory.decodeFile(inputFilePath, newOpts)
            newOpts.inJustDecodeBounds = false
            w = newOpts.outWidth
            h = newOpts.outHeight
            cropOptions = ImageUtil.getOptionCrop(w, h, maxScale)
            scale = ImageUtil.getOptionScale(cropOptions[0], cropOptions[1], width, height)
            newOpts.inSampleSize = ImageUtil.getOptionSample(scale)
            bitmap = BitmapFactory.decodeFile(inputFilePath, newOpts)
        } else {
            w = bitmap!!.width
            h = bitmap!!.height
            cropOptions = ImageUtil.getOptionCrop(w, h, maxScale)
            scale = ImageUtil.getOptionScale(cropOptions[0], cropOptions[1], width, height)
        }
        if (w == 0 || h == 0 || bitmap == null) return
        inDimension?.invoke(intArrayOf(w, h))
        val outWidth = (w / scale).toInt()
        val outHeight = (h / scale).toInt()
        if (scale > 1) bitmap = Bitmap.createScaledBitmap(bitmap!!, outWidth, outHeight, true)
        if (maxScale > 0) bitmap = Bitmap.createBitmap(
            bitmap!!,
            (cropOptions[2] / scale).toInt(),
            (cropOptions[3] / scale).toInt(),
            (cropOptions[0] / scale).toInt(),
            (cropOptions[1] / scale).toInt()
        )
        if (angle > 0) {
            val matrix = Matrix()
            matrix.setRotate(
                angle.toFloat(),
                bitmap!!.width.toFloat() / 2,
                bitmap!!.height.toFloat() / 2
            )
            bitmap =
                Bitmap.createBitmap(bitmap!!, 0, 0, bitmap!!.width, bitmap!!.height, matrix, true)
        }
        outDimension?.invoke(intArrayOf(bitmap!!.width, bitmap!!.height))
        val outB = bitmap!!.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(outB!!)
        if (background != 0) {
            canvas.drawColor(background)
        }
        canvas.drawBitmap(bitmap!!, 0f, 0f, null)
        val beforeFile: String = ImageUtil.getTempFile(context)
        try {
            val outputStream = FileOutputStream(beforeFile)
            outB.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val afterFile: String = ImageUtil.getTempFile(context)
        if (quality > 0)
            imageCompress(beforeFile, outputFilePath ?: afterFile, quality)
        else if (maxSize > 0)
            thumbnailCompress(beforeFile, outputFilePath ?: afterFile, maxSize)

        File(beforeFile).delete()
        if (outByteArray != null) {
            FileInputStream(outputFilePath).use { fis ->
                ByteArrayOutputStream().use { bos ->
                    val b = ByteArray(8 * 1024)
                    var n: Int
                    while (fis.read(b).also { n = it } != -1) {
                        bos.write(b, 0, n)
                    }
                    outByteArray.invoke(bos.toByteArray())
                }
            }
        }
        File(afterFile).delete()
        try {
            if (recycle && bitmap != null) {
                bitmap!!.recycle()
                bitmap = null
            }
            outB.recycle()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}