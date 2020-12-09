/*
 * Copyright (c) 2020. pinggonglve
 */

package me.pglvee.compress

import android.content.Context
import android.graphics.Bitmap
import me.pglvee.compress.ImageUtil.toPath
import java.io.File

class ImageData(val context: Context, val minSize: Long) {

    var inputFilePath: String? = null
        private set
    var outputFilePath: String? = null
        private set
    var inputBitmap: Bitmap? = null
        private set

    var width: Int = 0
        private set
    var height: Int = 0
        private set
    var quality: Int = 100
        private set
    var maxSize: Long = 0
        private set
    var angle: Int = 0
        private set
    var maxScale: Float = 0f
        private set
    var background: Int = 0
        private set

    fun src(file: File): ImageData {
        inputFilePath = file.absolutePath
        return this
    }

    fun src(path: String): ImageData {
        inputFilePath = path
        return this
    }

    fun src(bitmap: Bitmap): ImageData {
        inputBitmap = bitmap
        return this
    }

    fun src(data: ByteArray): ImageData {
        inputFilePath = data.toPath(context)
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

    /**
     * 设置图片的最大尺寸，超出尺寸则缩放该图片
     */
    fun dimension(width: Int, height: Int): ImageData {
        this.width = width
        this.height = height
        return this
    }

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
    fun maxSize(maxSize: Long): ImageData {
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