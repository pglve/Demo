/*
 * Copyright (c) 2020. pinggonglve
 */

package me.pglvee.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * created by 2020/8/30
 * @author pinggonglve
 **/

data class Response<T>(
    var data: T? = null,
    var msg: String? = null,
    var status: Int = 0
)

suspend fun <T> requestApi(api: suspend () -> Response<T?>): (Response<T?>) {
    return try {
        withContext(Dispatchers.IO) { api() }
    } catch (e: Exception) {
        Response(status = -1, msg = e.localizedMessage)
    }
}

inline fun <T, R> Response<T>.transform(transform: ((T?) -> R?)): (Response<R?>) {
    return Response(data = (transform(data)), msg, status)
}

inline fun <reified T> Response<T>.doSuccess(action: (T?) -> Unit): Response<T> {
    if (status == 0) action(data)
    return this
}

inline fun <reified T> Response<T>.doFailure(action: (Int) -> Unit): Response<T> {
    if (status != 0) action(status)
    return this
}

inline fun <reified T> Response<T>.doRetry(action: () -> Unit): Response<T> {
    if (status != 0) action()
    return this
}