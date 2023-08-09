package com.example.data.mappers

import com.example.data.models.parcelable.OccupationInfoParcel
import com.example.data.models.parcelable.UserInfoParcel
import com.example.domain.models.OccupationInfo
import com.example.domain.models.UserInfo

fun UserInfo.toParcel() =
    UserInfoParcel(
        userId,
        userName,
        country,
        occupationInfo = occupationInfo.toParcel()
    )

fun OccupationInfo.toParcel() =
    OccupationInfoParcel(
        name, place, holidaysPeriod
    )
