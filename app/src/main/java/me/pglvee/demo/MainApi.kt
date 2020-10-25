/*
 * Copyright (c) 2020. pinggonglve
 */

package me.pglvee.demo

import me.pglvee.database.entity.Book
import me.pglvee.network.BaseApi
import me.pglvee.network.Response
import retrofit2.http.GET
import retrofit2.http.Url

/**
 * created by 2020/8/30
 * @author pinggonglve
 **/

val MainApiService: MainApi
    get() = BaseApi.retrofit.create(MainApi::class.java)

const val URL = "https://www.gonglve.ml/api/data"

interface MainApi {

    @GET
    suspend fun find(@Url url: String = "${URL}/find/1"): Response<List<Book>?>
}