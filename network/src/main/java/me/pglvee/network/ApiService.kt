/*
 * Copyright (c) 2020. pinggonglve
 */

package me.pglvee.network

import retrofit2.http.GET
import retrofit2.http.Url

/**
 * created by 2020/8/30
 * @author pinggonglve
 **/
interface ApiService {

    @GET
    suspend fun login(@Url url: String): Response<String>


}