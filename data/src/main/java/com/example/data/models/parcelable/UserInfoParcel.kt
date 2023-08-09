package com.example.data.models.parcelable

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "UserInfo")
data class UserInfoParcel(
    @PrimaryKey val userId: String,
    val userName: String,
    val country: String,
    @Embedded val occupationInfo: OccupationInfoParcel
) : Parcelable