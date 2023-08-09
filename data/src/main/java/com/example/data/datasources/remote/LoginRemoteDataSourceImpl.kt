package com.example.data.datasources.remote

import com.example.data.datasources.remote.apiservices.LoginApiService
import com.example.data.executor.LoginRemoteDataSource
import com.example.data.utlis.handleErrorCode
import com.example.domain.DataSourceResultState
import com.example.domain.models.UserInfo
import com.example.domain.repositories.LoginErrorType

class LoginRemoteDataSourceImpl(private val apiService: LoginApiService) : LoginRemoteDataSource {
    override suspend fun postLoginUser(
        userName: String,
        userPass: String
    ): DataSourceResultState<UserInfo> {
        return try {
            val response = apiService.loginUser(userName, userPass)
            if (response.isSuccessful) {
                response.body()?.let { data ->
                    if (data.errorCode == "200") DataSourceResultState.Success(data.userInfo)
                    else DataSourceResultState.Error(handleErrorCode(data.errorCode))
                } ?: DataSourceResultState.Error(LoginErrorType.GeneralFailure)
            } else {
                DataSourceResultState.Error(LoginErrorType.GeneralFailure)
            }
        } catch (e: Exception) {
            /* Here we can get a more specific exception perhaps
            * e.g. IOException, NetworkException */
            DataSourceResultState.Error(LoginErrorType.GeneralFailure)
        }
    }
}