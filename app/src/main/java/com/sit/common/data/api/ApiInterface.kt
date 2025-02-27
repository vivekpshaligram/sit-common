package com.sit.common.data.api

import retrofit2.Response
import retrofit2.http.GET

interface ApiInterface {
    @GET("/posts")
    suspend fun getPostList(): Response<Any>
}