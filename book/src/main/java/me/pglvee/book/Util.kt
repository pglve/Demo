/*
 * Copyright (c) 2020. pinggonglve
 */

package me.pglvee.book

import android.content.res.Resources
import me.pglvee.base.BaseApp
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

fun readResource(resourceId: Int): String? { //读取资源文件，也可以读取Assets文件
    val builder = StringBuilder()
    try {
        val inputStream: InputStream = BaseApp.instance.resources.openRawResource(resourceId)
        val streamReader = InputStreamReader(inputStream)
        val bufferedReader = BufferedReader(streamReader)
        var textLine: String?
        while (bufferedReader.readLine().also { textLine = it } != null) {
            builder.append(textLine)
            builder.append("\n")
        }
    } catch (e: IOException) {
        e.printStackTrace()
    } catch (e: Resources.NotFoundException) {
        e.printStackTrace()
    }
    return builder.toString()
}