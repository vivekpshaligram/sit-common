package com.sit.sociallogin.data.remote

import kotlinx.coroutines.flow.Flow

interface Repository {

    fun getPostList(): Flow<Any>
}