package com.example.domain.models

data class UserInfo(
    val userId: String,
    val userName: String,
    val country: String,
    val occupationInfo: OccupationInfo
)