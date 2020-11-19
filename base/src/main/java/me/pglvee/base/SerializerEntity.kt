/*
 * Copyright (c) 2020. pinggonglve
 */

package me.pglvee.base

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

interface SerializerEntity<T> {

    fun writeTo(output: OutputStream)

    fun parseFrom(input: InputStream): T
}

inline fun <reified T : Any> new(): T {
    val clz = T::class.java
    val mCreate = clz.getDeclaredConstructor()
    mCreate.isAccessible = true
    return mCreate.newInstance()
}

inline fun <reified T> getSerializer(): Serializer<T> where T : SerializerEntity<T> {
    return object : Serializer<T> {
        override val defaultValue: T
            get() = new<T>()

        override fun readFrom(input: InputStream): T {
            try {
                return new<T>().parseFrom(input)
            } catch (exception: InvalidProtocolBufferException) {
                throw CorruptionException("Cannot read proto.", exception)
            }
        }

        override fun writeTo(t: T, output: OutputStream) = t.writeTo(output)
    }
}