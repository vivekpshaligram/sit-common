package com.sit.sociallogin.data.api

import retrofit2.Response
import retrofit2.http.GET

interface ApiInterface {
    @GET("/posts")
    suspend fun getPostList(): Response<Any>
}