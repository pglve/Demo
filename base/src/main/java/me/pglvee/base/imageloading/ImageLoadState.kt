/*
 * Copyright (c) 2021. pinggonglve
 */

package me.pglvee.base.imageloading

import androidx.compose.ui.graphics.painter.Painter

/**
 * Represents the state of a [ImageLoad]
 */
sealed class ImageLoadState {
    /**
     * Indicates that a request is not in progress.
     */
    object Empty : ImageLoadState()

    /**
     * Indicates that the request is currently in progress.
     */
    object Loading : ImageLoadState()

    /**
     * Indicates that the request completed successfully.
     *
     * @param painter The result image.
     * @param source The data source that the image was loaded from.
     */
    data class Success(
        val painter: Painter,
        val source: DataSource
    ) : ImageLoadState()

    /**
     * Indicates that an error occurred while executing the request.
     *
     * @param painter The error image.
     * @param throwable The optional throwable that caused the request failure.
     */
    data class Error(
        val painter: Painter? = null,
        val throwable: Throwable
    ) : ImageLoadState()
}