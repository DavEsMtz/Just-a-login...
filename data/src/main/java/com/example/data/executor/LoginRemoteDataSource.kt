package com.example.data.executor

import com.example.domain.DataSourceResultState
import com.example.domain.models.UserInfo

interface LoginRemoteDataSource {
    suspend fun postLoginUser(userName: String, userPass: String): DataSourceResultState<UserInfo>
}