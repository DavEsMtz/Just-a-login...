package com.example.data.models

import com.example.domain.models.UserInfo


data class LoginUserResponse(
    val errorCode: String,
    val userInfo: UserInfo
)