/*
 * Copyright (c) 2020. pinggonglve
 */

package me.pglvee.base

import java.io.InputStream
import java.io.OutputStream

data class Settings(
    var name: String = ""
) : SerializerEntity<Settings> {
    override fun writeTo(output: OutputStream) {
        TODO("Not yet implemented")
    }

    override fun parseFrom(input: InputStream): Settings {
        TODO("Not yet implemented")
    }

}