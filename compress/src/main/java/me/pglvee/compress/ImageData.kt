/*
 * Copyright (c) 2020. pinggonglve
 */

package me.pglvee.compress

import android.content.Context
import android.graphics.Bitmap
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream

data class ImageData(
    var context: Context,
    var inputFilePath: String? = null,
    var outputFilePath: String? = null,
    var bitmap: Bitmap? = null,

    var width: Int = 0,
    var height: Int = 0,
    var quality: Int = 100,
    var maxSize: Long = 0,
    var angle: Int = 0,
    var maxScale: Float = 0f,
    var background: Int = 0
) {

    fun src(file: File): ImageData {
        inputFilePath = file.absolutePath
        return this
    }

    fun src(path: String): ImageData {
        inputFilePath = path
        return this
    }

    fun src(b: Bitmap): ImageData {
        bitmap = b
        return this
    }

    fun src(data: ByteArray): ImageData {
        val file =
            File.createTempFile(System.currentTimeMillis().toString(), ".jpg", context.cacheDir)
        FileOutputStream(file).use { fos ->
            BufferedOutputStream(fos).use { bos ->
                bos.write(data)
            }
        }
        inputFilePath = file.path
        return this
    }

    fun dst(file: File): ImageData {
        outputFilePath = file.absolutePath
        return this
    }

    fun dst(path: String): ImageData {
        outputFilePath = path
        return this
    }
//
//    fun dst(var data: ByteArray): ImageData {
//        FileInputStream(outputFilePath).use { fis ->
//            ByteArrayOutputStream().use { bos ->
//                val b = ByteArray(8 * 1024)
//                var n: Int
//                while (fis.read(b).also { n = it } != -1) {
//                    bos.write(b, 0, n)
//                }
//                fis.close()
//                bos.close()
//                data = bos.toByteArray()
//            }
//        }
//    }


    /**
     * 设置图片最大比例，超出比例裁剪图片
     */
    fun crop(maxScale: Float): ImageData {
        this.maxScale = maxScale
        return this
    }

    /**
     * 设置背景颜色，对于带透明度的png图片可以设置为白底或黑底
     */
    fun background(color: Int): ImageData {
        background = color
        return this
    }

    /**
     * 自动旋转图片
     */
    fun rotate(): ImageData {
        this.angle = ImageUtil.readPictureDegree(inputFilePath)
        return this
    }

    /**
     * 根据角度旋转图片
     */
    fun rotate(angle: Int): ImageData {
        this.angle = angle
        return this
    }

    /**
     * 限制输出图片的最大大小，使用[.image]方法时无效
     */
    fun max(maxSize: Long): ImageData {
        this.maxSize = maxSize
        return this
    }

    /**
     * 设置输出图片的质量，使用[.thumbnail]方法时无效
     */
    fun quality(quality: Int): ImageData {
        this.quality = quality
        return this
    }
}