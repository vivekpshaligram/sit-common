package com.sit.sociallogin.data.remote

import com.sit.sociallogin.data.api.ApiInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val projectService: ApiInterface) : Repository {

    override fun getPostList(): Flow<Any> {
        return flow { emit(projectService.getPostList()) }
    }
}