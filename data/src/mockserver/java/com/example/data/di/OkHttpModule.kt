package com.example.data.di

import android.content.Context
import com.mustafayigit.mockresponseinterceptor.MockResponseInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class OkHttpModule {

    @Provides
    fun provideMockResponseInterceptor(
        @ApplicationContext context: Context
    ): MockResponseInterceptor {
        return MockResponseInterceptor.Builder(context.assets)
            .isGlobalMockingEnabled { true }
            .build()
    }

    @Singleton
    @Provides
    fun providesOkhttpClient(mockResponseInterceptor: MockResponseInterceptor) =
        OkHttpClient.Builder()
            .addInterceptor(mockResponseInterceptor)
            .addInterceptor(
                HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY)
            ).build()
}