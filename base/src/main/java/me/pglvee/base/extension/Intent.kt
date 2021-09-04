/*
 * Copyright (c) 2021. pinggonglve
 */

package me.pglvee.base.extension

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable

/**
 * Returns a new [Intent] with the given key/value pairs as elements.
 *
 * @throws IllegalArgumentException When a value is not a supported type of [Intent].
 */
fun intentOf(vararg pairs: Pair<String, Any?>) = Intent().apply {
    for ((key, value) in pairs) {
        when (value) {
            null -> putExtra(key, "") // Any nullable type will suffice.

            // Scalars
            is Boolean -> putExtra(key, value)
            is Byte -> putExtra(key, value)
            is Char -> putExtra(key, value)
            is Double -> putExtra(key, value)
            is Float -> putExtra(key, value)
            is Int -> putExtra(key, value)
            is Long -> putExtra(key, value)
            is Short -> putExtra(key, value)

            // References
            is Bundle -> putExtra(key, value)
            is CharSequence -> putExtra(key, value)
            is Parcelable -> putExtra(key, value)

            // Scalar arrays
            is BooleanArray -> putExtra(key, value)
            is ByteArray -> putExtra(key, value)
            is CharArray -> putExtra(key, value)
            is DoubleArray -> putExtra(key, value)
            is FloatArray -> putExtra(key, value)
            is IntArray -> putExtra(key, value)
            is LongArray -> putExtra(key, value)
            is ShortArray -> putExtra(key, value)

            // Reference arrays
            is ArrayList<*> -> {
                val componentType = value::class.java.componentType!!
                @Suppress("UNCHECKED_CAST") // Checked by reflection.
                when {
                    Parcelable::class.java.isAssignableFrom(componentType) -> {
                        putParcelableArrayListExtra(key, value as ArrayList<Parcelable>)
                    }
                    String::class.java.isAssignableFrom(componentType) -> {
                        putStringArrayListExtra(key, value as ArrayList<String>)
                    }
                    CharSequence::class.java.isAssignableFrom(componentType) -> {
                        putCharSequenceArrayListExtra(key, value as ArrayList<CharSequence>)
                    }
                    Int::class.java.isAssignableFrom(componentType) -> {
                        putIntegerArrayListExtra(key, value as ArrayList<Int>)
                    }
                    else -> {
                        val valueType = componentType.canonicalName
                        throw IllegalArgumentException(
                            "Illegal value array type $valueType for key \"$key\""
                        )
                    }
                }
            }

            // Last resort. Also we must check this after Array<*> as all arrays are serializable.
            is Serializable -> putExtra(key, value)

            else -> error("Illegal value \"$value\" for key \"$key\"")
        }
    }
}