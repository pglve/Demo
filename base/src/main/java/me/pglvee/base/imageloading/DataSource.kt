/*
 * Copyright (c) 2021. pinggonglve
 */

package me.pglvee.base.imageloading

import android.graphics.Bitmap
import androidx.annotation.DrawableRes
import java.io.File
import java.nio.ByteBuffer

enum class DataSource {
    /**
     * Represents an in-memory data source or cache (e.g. [Bitmap], [ByteBuffer]).
     */
    MEMORY,

    /**
     * Represents a disk-based data source (e.g. [DrawableRes], [File]).
     */
    DISK,

    /**
     * Represents a network-based data source.
     */
    NETWORK
}