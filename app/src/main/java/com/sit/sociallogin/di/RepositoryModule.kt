package com.sit.sociallogin.di

import com.sit.sociallogin.data.remote.Repository
import com.sit.sociallogin.data.remote.RepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun provideRemoteRepository(remoteRepositoryImpl: RepositoryImpl): Repository
}