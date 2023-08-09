package com.example.data.repositories

import com.example.data.datasources.local.dao.UserDao
import com.example.data.executor.LoginRemoteDataSource
import com.example.data.mappers.toParcel
import com.example.domain.DataSourceResultState
import com.example.domain.models.UserInfo
import com.example.domain.repositories.LoginRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


class LoginRepositoryImpl(
    private val localDataSource: UserDao,
    private val remoteDataSource: LoginRemoteDataSource
) : LoginRepository {
    override suspend fun loginUser(
        userName: String,
        userPass: String
    ): Flow<DataSourceResultState<UserInfo>> {
        return flow {
            emit(remoteDataSource.postLoginUser(userName, userPass))
        }.flowOn(Dispatchers.IO)
    }

    // Will be use in the future...
    override suspend fun saveUser(userInfo: UserInfo) {
        localDataSource.insert(userInfo.toParcel())
    }
}