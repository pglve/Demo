/*
 * Copyright (c) 2020. pinggonglve
 */

package me.pglvee.protobuf

import me.pglvee.generation.protobuf.LogPreferences
import me.pglvee.protobuf.entity.LogPreferencesEntity
import me.pglvee.protobuf.entity.UserInfo
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testProtoConvertor() {
        val localTestData = LogPreferencesEntity(
            id = 898,
            type = 2,
            token = "ad98798ad87a89d8ca",
            UserInfo(
                name = "LiLi",
                avatar = "/avatar/test.jpg",
                age = 20
            )
        )
        val localProtoData = localTestData.writeTo()
        //write
        val protoData = LogPreferences.ADAPTER.encode(localProtoData)
        println("protobuf content： ${String(protoData)}")
        //parse
        val parseData = LogPreferences.ADAPTER.decode(protoData)
        println("entity content： $parseData")
    }
}