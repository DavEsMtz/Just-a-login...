package com.example.domain.usecases

import com.example.domain.DataSourceResultState
import com.example.domain.models.UserInfo
import com.example.domain.repositories.LoginRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: LoginRepository
) {
    suspend operator fun invoke(
        userName: String, userPass: String
    ): Flow<DataSourceResultState<UserInfo>> =
        repository.loginUser(userName, userPass)
}