package com.example.domain.repositories

import com.example.domain.DataSourceResultState
import com.example.domain.models.UserInfo
import kotlinx.coroutines.flow.Flow

/**
 * Bridge for interaction between LoginRepositoryImpl and LoginUseCase
 */

interface LoginRepository {
    suspend fun loginUser(userName: String, userPass: String): Flow<DataSourceResultState<UserInfo>>
    suspend fun saveUser(userInfo: UserInfo)
}