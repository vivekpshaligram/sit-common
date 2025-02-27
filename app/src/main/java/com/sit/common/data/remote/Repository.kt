package com.sit.common.data.remote

import kotlinx.coroutines.flow.Flow

interface Repository {

    fun getPostList(): Flow<Any>
}