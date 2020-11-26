/*
 * Copyright (c) 2020. pinggonglve
 */

package me.pglvee.protobuf.entity

import me.pglvee.generation.protobuf.LogPreferences
import okio.ByteString.Companion.EMPTY
import okio.ByteString.Companion.encodeUtf8

data class LogPreferencesEntity(
    var id: Long = 0L,
    var type: Int = 0,
    var token: String? = "",
    var userInfo: UserInfo? = null
) {
    fun parseFrom(from: LogPreferences): LogPreferencesEntity {
        this.id = from.id
        this.type = from.type.value
        this.token = from.token
        this.userInfo = UserInfo().parseFrom(from.user_info)
        return this
    }

    fun writeTo(): LogPreferences {
        return LogPreferences(
            id,
            LogPreferences.Type.fromValue(type) ?: LogPreferences.Type.UNKNOWN,
            token ?: "",
            userInfo?.writeTo()
        )
    }
}

data class UserInfo(
    var name: String? = "",
    var avatar: String? = "",
    var age: Int = 0
) {
    fun parseFrom(from: LogPreferences.UserInfo?): UserInfo {
        this.name = from?.name?.let { String(it.toByteArray()) }
        this.avatar = from?.avatar
        return this
    }

    fun writeTo(): LogPreferences.UserInfo {
        return LogPreferences.UserInfo(
            name?.encodeUtf8() ?: EMPTY,
            avatar ?: "",
            age
        )
    }
}