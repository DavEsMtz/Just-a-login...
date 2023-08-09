package com.example.data.di

import com.example.data.datasources.local.dao.UserDao
import com.example.data.executor.LoginRemoteDataSource
import com.example.data.datasources.remote.LoginRemoteDataSourceImpl
import com.example.data.datasources.remote.apiservices.LoginApiService
import com.example.data.repositories.LoginRepositoryImpl
import com.example.domain.repositories.LoginRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
class LoginModule {

    @Provides
    fun providesLoginApiService(retrofit: Retrofit): LoginApiService =
        retrofit.create(LoginApiService::class.java)

    @Provides
    fun providesLoginRemoteDataSource(apiService: LoginApiService): LoginRemoteDataSource =
        LoginRemoteDataSourceImpl(apiService)

    @Provides
    fun providesLoginRepository(
        localDataSource: UserDao,
        remoteDataSource: LoginRemoteDataSource
    ): LoginRepository {
        return LoginRepositoryImpl(localDataSource, remoteDataSource)
    }
}