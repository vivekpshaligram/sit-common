package com.sit.common.di

import android.content.Context
import com.sit.common.preference.CommonPreferenceManager
import com.sit.common.utils.PrintLogManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ManagerModule {

    @Provides
    @Singleton
    fun providePreferenceManager(
        @ApplicationContext context: Context,
    ): CommonPreferenceManager {
        return CommonPreferenceManager(context)
    }

    @Provides
    @Singleton
    fun providePrintLog(
        @ApplicationContext context: Context,
        loggingEnable: Boolean,
    ): PrintLogManager {
        val printLogManager = PrintLogManager(context, loggingEnable)
        return printLogManager
    }
}